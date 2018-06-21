package com.gw.dao;

import com.gw.entity.Gts2;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public interface Gts2Dao {
    List<Gts2> find(Query query,String collectionName, String key);
}
