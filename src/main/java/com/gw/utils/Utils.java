package com.gw.utils;

import com.gw.entity.Gts2PermissionConfig;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenjin on 2018/6/2.
 * 公共类
 */
public class Utils {
    public static void main(String[] args) {
        //List<Gts2PermissionConfig> list=new ArrayList<Gts2PermissionConfig>();

        Gts2PermissionConfig configs =new Gts2PermissionConfig();
        HashMap<String,Object> condition=new HashMap<>();
        ArrayList company_id_list =new ArrayList();
        company_id_list.add(5);company_id_list.add("6");
        condition.put("object.company_id",company_id_list);
        condition.put("object.id",7.0);
        condition.put("object.name","tom");
        configs.setCondition(condition);
        //list.add(configs);
        Query query = new Query();
        Map<String, Object> map = configs.getCondition();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            System.out.println("Key = " + key + ", Value = " + value+","+value.getClass().getName());
            if(value instanceof ArrayList){ //判断是何种数据类型
                query.addCriteria(Criteria.where(key).in((ArrayList)value));
            }else{
                query.addCriteria(Criteria.where(key).is(value));
            }



        }

        //query.addCriteria(Criteria.where("user").is("test").and("pwd").is("test"));
        //query.addCriteria(Criteria.where("collectionName").is("gts2_t_clone_trade_real"));
        System.out.println(query.toString());


        /**
         * db.getCollection('gts2_t_clone_customer_account_info_real')
         .find({"object.application_date" : {$gte:"2018-04-13 07:07:14", $lte:"2018-04-13 07:07:14"}
         ,"object.account_no_int":456789})

         object.application_date=gte:2018-04-13 07:07:14,lte:2018-04-13 07:07:14
         &object.application_date=in:sadasda,asdasda,asdasda
         &object.account_no_int=456789
         &limit=1213
         */
        query=new Query();
        String condition_s ="object.application_date@@gte@2018-04-13 07:07:14,lte@2018-04-13 07:07:14@@@object.application_name@@in@test1,test2,test2@@@object.account_no_int@@456789";
        String[] first_c = condition_s.split("@@@");
        for(String s:first_c){
            System.out.println(s);
            String[] second_c = s.split("@@");
            System.out.println(second_c[0]+"----"+second_c[1]);
            if(second_c[1].contains("gte")){

            }
            String[] values = second_c[1].split("@");
            Criteria cri = new Criteria(second_c[0]);
            for (String v:values){
                //System.out.println(v);
                //cri.i
            }
        }
        //System.out.println(Example.of("\"{ \"object.company_id\" : { \"$in\" : [ 5.0 , 6.0]}}\"").getProbe());
        //query.addCriteria(Criteria.where("a").is("1").orOperator(Criteria.where("b").is("2").and("c").is(3)));

        //query.addCriteria(Criteria.byExample("{ \"object.company_id\" : { \"$in\" : [ 5.0 , 6.0]}}"));
        System.out.println(query.toString());

        BasicQuery query1 = new BasicQuery("{ age : { $lt : 50 }, accounts.balance : { $gt : 1000.00 },object.company_id:6,tags:{$regex:'run'}}");
        System.out.println(query1.toString());
        query1.addCriteria(Criteria.where("object.company_id").in(new Integer[]{1,2}));
        System.out.println(query1.toString());



        System.out.println("gt@2018-04-13 07:07:14,gt@2018-04-13 07:07:14".matches("^(.*gt.*)|(.*lt.*)|(.*gte.*)|(.*lte.*)|(.*ne.*)$"));

        Object s ="133";
        System.out.println(s instanceof Integer);
    }

    /**
     *  去除空格以及换行符
     \n 回车(\u000a)
     \t 水平制表符(\u0009)
     \s 空格(\u0008)
     \r 换行(\u000d)
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            //Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Pattern p = Pattern.compile("\\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

}
