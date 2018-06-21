package com.gw.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 统一标准实体
 */
public class Gts2 implements Serializable{
    private static final long serialVersionUID = -1871970758239560092L;
    //@Id
    //private String _id;
    private HashMap object;

    private HashMap meta;

    public HashMap getMeta() {
        return meta;
    }

    public void setMeta(HashMap meta) {
        this.meta = meta;
    }

    public HashMap getObject() {
        return object;
    }

    public void setObject(HashMap object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "Gts2{" +
                "object=" + object +
                ", meta='" + meta + '\'' +
                '}';
    }
}
