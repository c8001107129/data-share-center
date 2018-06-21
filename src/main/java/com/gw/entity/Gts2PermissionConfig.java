package com.gw.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 权限配置映射实体
 */
@Document(collection = "gts2_config")
public class Gts2PermissionConfig implements Serializable {

    private static final long serialVersionUID = 5166617866827621563L;

    @Id
    private String _id;
    //用户名
    private String user;
    //密码
    private String pwd;
    //集合名称
    private String collectionName;
    //查询条件
    private HashMap<String,Object> condition;
    //查询记录最大数
    private Integer limit;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public HashMap getCondition() {
        return condition;
    }

    public void setCondition(HashMap condition) {
        this.condition = condition;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "Gts2PermissionConfig{" +
                "_id='" + _id + '\'' +
                ", user='" + user + '\'' +
                ", pwd='" + pwd + '\'' +
                ", collectionName='" + collectionName + '\'' +
                ", condition=" + condition +
                ", limit=" + limit +
                '}';
    }
}
