package com.gw.entity;

import java.io.Serializable;

/**
 * 统一返回结果实体
 */
public class ResponseStatement implements Serializable{
    private static final long serialVersionUID = 3630373342630406190L;
    private String status;//状态标识 OK=成功、FAIL=失败
    private String message;//错误消息
    //private Integer all_num;
    //private Integer page_num;
    private Object data;//返回数据结果，错误没有

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
