package com.gw.dao.impl;

import com.gw.dao.Gts2Dao;
import com.gw.entity.Gts2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Gts2DaoImpl implements Gts2Dao{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier(value = "primaryMongo")
    protected MongoTemplate mongoTemplate;

    @Override
    //@Cacheable(value = "gts2", key = "#collectionName+#key")
    public List<Gts2> find(Query query,String collectionName, String key) {
        logger.info("find...");
        return mongoTemplate.find(query,Gts2.class,collectionName);
    }
}
