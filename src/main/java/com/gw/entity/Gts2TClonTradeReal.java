package com.gw.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.HashMap;

@Document(collection = "gts2_t_clone_trade_real")
public class Gts2TClonTradeReal implements Serializable{

    private static final long serialVersionUID = -1956116346021396040L;
    @Id
    private String _id;

    private HashMap object;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public HashMap getObject() {
        return object;
    }

    public void setObject(HashMap object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "Gts2TClonTradeReal{" +
                "_id='" + _id + '\'' +
                ", object='" + object + '\'' +
                '}';
    }
}
