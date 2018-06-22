package com.gw.controller;

import com.gw.dao.impl.Gts2DaoImpl;
import com.gw.entity.Gts2;
import com.gw.entity.Gts2PermissionConfig;
import com.gw.entity.ResponseStatement;
import com.gw.utils.ListPageUtil;
import com.gw.utils.Utils;
import com.mongodb.util.JSONParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/gts2")
public class Gts2Controller {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 由springboot自动注入，默认配置会产生mongoTemplate这个bean
     */
    //@Autowired
    //private MongoTemplate mongoTemplate;
    // Using MongoTemplate for primary database
    @Autowired
    @Qualifier(value = "primaryMongo")
    protected MongoTemplate mongoTemplate;
    //protected Gts2DaoImpl mongoTemplate;

    // Using mongoTemplate for secondary database
    @Autowired
    @Qualifier(value = "secondaryMongo")
    protected MongoTemplate secondaryMongoTemplate;

    //过滤关键字集合
    private String[] keys = new String[]{
            "$regex",
            "$search",
            "$text"
    };

    /**
     *
     * @param user 用户账号
     * @param pwd 用户密码
     * @param collectionName 集合名称
     * @param condition 查询条件
     * @param limit 查询限制 记录数
     * //@param read_num 每次读取记录数
     * //@param clean_cache 清除缓存标记，默认参数不传或传0，不清除缓存，-1=操作前清除缓存
     * //@param request
     * @return
     */
    @RequestMapping("/find")
    ResponseStatement find(String user,
                           String pwd,
                           String collectionName,
                           @RequestParam(defaultValue = "{}") String condition,
                           @RequestParam(defaultValue = "100000") Integer limit
                           //@RequestParam(defaultValue = "0") Integer read_num,
                           //@RequestParam(defaultValue = "0") Integer clean_cache,
                           //@RequestParam(defaultValue = "0") Integer page_num,// todo 页码参数的功能暂时没有实现
                           //HttpServletRequest request
    ){
        long start_time = System.currentTimeMillis();
        condition = Utils.replaceBlank(condition);
        logger.debug("parames,user:{},pwd:{},condition:{},collectionName:{},limit:{}",user,pwd,condition,collectionName,limit);
        //返回信息实体
        ResponseStatement statement=new ResponseStatement();
        //step0 请求参数验证
        if(StringUtils.isEmpty(user) || StringUtils.isEmpty(pwd) || StringUtils.isEmpty(collectionName)){
            statement.setStatus("FAIL");
            statement.setMessage("参数：user、pwd、collectionName必须填写，且不能为空字符串！");
            return statement;
        }
        //异常捕获
        try {
            //step1 验证集合中的用户查询权限
            Query query_config = new Query(Criteria.where("user").is(user)
                    .and("pwd").is(pwd).and("collectionName").is(collectionName));
            logger.debug("query_config:{}",query_config.toString());
            Gts2PermissionConfig configs=secondaryMongoTemplate.findOne(query_config, Gts2PermissionConfig.class);
            logger.debug("config:{}",configs);
            if(configs!=null){
                //step1-1 验证用户limit权限
                //如果传入返回记录数大于配置权限中的记录数，直接返回错误
                if(limit.intValue()>configs.getLimit().intValue()){
                    statement.setStatus("FAIL");
                    statement.setMessage("您一次无法查询("+limit+")条记录，请按照管理员给您配置的最大记录数填写limit");
                }else {
                    //指定返回字段
                    String fields="{'object':1,'_id':0,'meta.kafka.receivedTime':1}";
                    //step2 组合传入的条件信息
                    Query query =  new BasicQuery(condition,fields);//StringUtils.isEmpty(condition) ? new BasicQuery("{}",fields):new BasicQuery(condition,fields);
                    logger.debug("1:{}",query.toString());
                    //过滤某些关键字：如 $regex
                    for (String key:keys){
                        if(condition.contains(key)){
                            statement.setStatus("FAIL");
                            statement.setMessage("暂时不支持关键字("+key+")");
                            return statement;
                        }
                    }
                    //step3 提取权限表的配置信息 提取过滤条件
                    Map<String, Object> map = configs.getCondition();
                    if(map!=null && map.size()>0){
                        initConfigCondition(query,configs.getCondition());
                    }
                    //step4 正式查询
                    query.limit(limit);
                    logger.debug("query,{}",query.toString());
                    List<Gts2> obj = mongoTemplate.find(query, Gts2.class,collectionName);
                    statement.setData(obj);
                    //statement.setAll_num(obj.size());
                    //statement.setPage_num(1); //这里没有做分页，所以默认1
                    int count = obj.size();
                    /*
                    List<Gts2> obj = null;
                    int count = 0; //记录每次获取的总记录数
                    if(read_num.intValue() == 0){ //这里如果没有参数就走原来的方式
                        query.limit(limit);
                        logger.debug("query,{}",query.toString());
                        obj = mongoTemplate.find(query, Gts2.class,collectionName);
                        statement.setData(obj);
                        statement.setAll_num(obj.size());
                        statement.setPage_num(1); //这里没有做分页，所以默认1
                        count = obj.size();
                    }else {
                        //获取session对象
                        HttpSession session = request.getSession();
                        if(clean_cache.intValue() == -1){ //如果-1操作前清除缓存
                            clean_cache(session, user, collectionName);
                        }
                        logger.debug("maxInactiveInterval:{}",session.getMaxInactiveInterval());
                        Object s_user_collection = session.getAttribute(user+collectionName); //user+collectionName
                        ListPageUtil pageList = null;
                        //如果在session找到对应的存储内容
                        if(s_user_collection!=null){
                            //session中提取缓存数据
                            pageList= (ListPageUtil) s_user_collection;
                            int _page_num = (int) session.getAttribute(user+collectionName+"_page_num");
                            int _all_num = (int) session.getAttribute(user+collectionName+"_all_num");
                            logger.debug("before：进入session查询,{},{}",_all_num,_page_num);
                            if(pageList.getPageCount()>_page_num){
                                _page_num+=1; //增加一页
                                if(_page_num == pageList.getPageCount()){
                                    //删除session数据
                                    clean_cache(session, user, collectionName);
                                }else { //记录当前已经是第几页数据了
                                    session.setAttribute(user+collectionName+"_page_num", _page_num);
                                }
                            }
                            List data = pageList.getPagedList(_page_num);
                            statement.setData(data);
                            statement.setAll_num(_all_num);
                            statement.setPage_num(_page_num);
                            logger.debug("after：进入session查询,{},{}",_all_num,_page_num);
                            count = data.size();
                        }else {
                            query.limit(limit);
                            logger.debug("query,{}",query.toString());
                            obj = mongoTemplate.find(query, Gts2.class,collectionName);
                            //如果结果只有一页那么就直接返回,不需要做session缓存了
                            if(obj!=null && obj.size()>0 && obj.size()> read_num){
                                //分页操作
                                pageList =new ListPageUtil<Gts2>(obj,read_num);
                                int _page_num = 1;
                                //设置session
                                session.setAttribute(user+collectionName,pageList);
                                session.setAttribute(user+collectionName+"_all_num", pageList.getAllCount());
                                session.setAttribute(user+collectionName+"_page_num", _page_num);
                                //返回结果赋值
                                statement.setData(pageList.getPagedList(_page_num));
                                statement.setAll_num(pageList.getAllCount());
                                statement.setPage_num(_page_num);
                                count = read_num;
                            }else{ //如果没有数据那就返回，空数组结果
                                statement.setData(obj);
                                statement.setAll_num(obj.size());
                                statement.setPage_num(1);//这里数据不足分页操作，因此默认是1页
                                count = obj.size();
                            }
                        }
                    }
                    */
                    statement.setStatus("OK");
                    //statement.setData(obj);
                    logger.info("{},{},{},{}",System.currentTimeMillis()-start_time,count,user,collectionName);
                }

            }else {
                statement.setStatus("FAIL");
                statement.setMessage("用户名或密码错误，或无权操作集合("+collectionName+")");
            }
        }catch (JSONParseException jsonParseException){
            statement.setStatus("FAIL");
            statement.setMessage("参数：condition，必须是标准的json格式！");
            logger.error("参数：condition必须是标准的json格式：{}",jsonParseException);
        }catch (Exception e){
            statement.setStatus("FAIL");
            statement.setMessage("数据获取异常,请联系管理员！");
            logger.error("数据获取异常：{}",e);
            //e.printStackTrace();
        }
        return statement;
    }

    /**
     * 清除缓存
     * @param session
     * @param user
     * @param collectionName
     */
    private void clean_cache(HttpSession session, String user, String collectionName) {
        session.removeAttribute(user+collectionName);
        session.removeAttribute(user+collectionName+"_all_num");
        session.removeAttribute(user+collectionName+"_page_num");
    }

    /**
     * 初始化权限配置表中的条件
     * @param query
     * @param condition
     */
    private void initConfigCondition(Query query, HashMap<String,Object> condition) {
        for (Map.Entry<String, Object> entry : condition.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            logger.debug("Key = " + key + ", Value = " + value+","+value.getClass().getName());
            //判断是何种类型，看是数组类型还是
            if(value instanceof ArrayList){
                query.addCriteria(Criteria.where(key).in((ArrayList)value));
            }else{
                query.addCriteria(Criteria.where(key).is(value));
            }
        }
    }


    /**
     * 这个暂时不支持 or条件
     * @param user
     * @param pwd
     * @param connectionName
     * @param condition
     * @param limit
     * @param request
     * @return
     *//*
    @Deprecated
    @RequestMapping("/findv1")
    ResponseStatement findv1(String user,
                           String pwd,
                           String collectionName,
                           @RequestParam(defaultValue = "") String condition,
                           @RequestParam(defaultValue = "100000") Integer limit,
                           HttpServletRequest request){
        logger.info("user:{},pwd:{},condition:{},collectionName:{},limit:{}",user,pwd,condition,collectionName,limit);
        //返回信息实体
        ResponseStatement statement=new ResponseStatement();
        //step0 请求参数验证
        if(StringUtils.isEmpty(user) || StringUtils.isEmpty(pwd) || StringUtils.isEmpty(collectionName)){
            statement.setStatus("FAIL");
            statement.setMessage("参数：user、pwd、collectionName必须填写，且不能为空字符串！");
            return statement;
        }
        //异常捕获
        try {
            //step1 验证集合中的用户查询权限
            Query query_config = new Query(Criteria.where("user").is(user)
                    .and("pwd").is(pwd).and("collectionName").is(collectionName));
            logger.info("query_config:{}",query_config.toString());
            Gts2PermissionConfig configs=secondaryMongoTemplate.findOne(query_config, Gts2PermissionConfig.class);
            logger.info("config:{}",configs);
            if(configs!=null){
                //step1-1 验证用户limit权限
                if(limit.intValue()>configs.getLimit().intValue()){//如果传入返回记录数大于配置权限中的记录数，直接返回错误
                    statement.setStatus("FAIL");
                    statement.setMessage("您一次无法查询("+limit+")条记录，请按照管理员给您配置的最大记录数填写limit");
                }else {
                    //查询条件组合
                    Query query =  new Query();
                    //step2 提取权限表的配置信息 提取过滤条件
                    Map<String, Object> map = configs.getCondition();
                    if(map!=null && map.size()>0){
                        initConfigCondition(query,configs.getCondition());
                    }
                    //step3 组合传入的条件信息

                    Enumeration<String> parmes = request.getParameterNames();
                    while (parmes.hasMoreElements()){
                        String p = parmes.nextElement();
                        if(p.startsWith("object.")){  //以object.开头的
                            //将权限配置中的条件过滤掉
                            if(!map.containsKey(p)){
                                String v = request.getParameter(p);
                                logger.info(p+"--"+v);
                                Criteria cri = getCriteria(p,v);
                                //增加条件
                                query.addCriteria(cri);
                            }

                        }

                    }
                    //BasicQuery query1 = new BasicQuery("{ \"object.company_id\" : { \"$in\" : [ 5.0 , 6.0]} , \"object.application_date\" : { \"$gte\" : \"2018-04-13 08:07:14\"} , \"object.account_no_int\" : 456789.0 , \"object.application_name\" : { \"$in\" : [ \"test1\" , \"test2\"]}}");
                    query.limit(limit);
                    logger.info(query.toString());
                    List<Gts2> obj=mongoTemplate.find(query, Gts2.class,collectionName);
                    statement.setStatus("OK");
                    statement.setData(obj);
                }


            }else {
                statement.setStatus("FAIL");
                statement.setMessage("用户名或密码错误，或无权操作集合("+collectionName+")");
            }
        }catch (Exception e){
            statement.setStatus("FAIL");
            statement.setMessage("数据获取异常");
            logger.error("数据获取异常：{}",e.getMessage());
            e.printStackTrace();
        }
        return statement;
    }

    *//**
     * 生成任意条件组合
     * @param p 参数名
     * @param v 参数值
     * @return
     *//*
    private Criteria getCriteria(String p, String v) {
        Criteria cri=new Criteria(p);
        //正则表带是判断条件
        if(v.matches("^(.*gt@.*)|(.*lt@.*)|(.*gte@.*)|(.*lte@.*)|(.*ne@.*)$")){
            String[] vv = v.split(",");
            for (String s:vv){
                String[] vs =s.split("@");
                if("gte".equals(vs[0])){ //大于等于
                    cri.gte(convert(vs[1]));
                }else if("lte".equals(vs[0])){ //小于等于
                    cri.lte(convert(vs[1]));
                }else if("lt".equals(vs[0])){ //小于
                    cri.lt(convert(vs[1]));
                }else if("gt".equals(vs[0])){ //大于
                    cri.gt(convert(vs[1]));
                }else if("ne".equals(vs[0])){ //不等于
                    cri.ne(convert(vs[1]));
                }
            }
        }else if(v.matches("^(.*in@.*)$")){
            String[] vv = v.split("@");
            Object obj=convert(vv[1].split(","));
            if("in".equals(vv[0])){ //在其中
                //强制转换称ArrayList类型，不然组合的in条件就错了
                cri.in((ArrayList)obj);
            }else if("nin".equals(vv[0])){ //不在其中
                cri.nin((ArrayList)obj);
            }
        }else {
            cri.is(convert(v));
        }
        return cri;
    }

    *//**
     * 由于mongdb对值的类型有严格的要求，这里暂时只能这样操作了，将传入的数字全部转化称Double类型
     * @param obj
     * @return
     *//*
    private Object convert(Object obj){
        //logger.info("convert:{}",obj.getClass().getName());
        //
        if(obj instanceof String){
            try {
                return Double.parseDouble(obj.toString());
            }catch (Exception e){
                return obj;
            }
        }else if(obj instanceof String[]){
            String[] ss = (String[]) obj;
            ArrayList list =new ArrayList();
            for(String s:ss){
                try {
                    list.add(Double.parseDouble(s));
                }catch (Exception e){
                    list.add(s);
                }
            }
            return list;
        }
        return obj;
    }*/
}
