package com.gw.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;

/**
 * 主数据源：
 */
@Configuration  //Configuration class
@EnableAutoConfiguration
@EnableMongoRepositories(basePackages = "com.gw.config.primary", mongoTemplateRef = "primaryMongo")
public class PrimaryMongoTemplate {
    @Autowired
    @Qualifier("primaryMongoProperties")
    private MongoProperties mongoProperties;

    /**
     * 注意：Bean 的名称与方法名称不要一样，如果一样启动的时候会出错，导致无法启动程序
     * @return
     * @throws Exception
     */
    @Primary
    @Bean(name = "primaryMongo")
    public MongoTemplate primaryMongoTemplate() throws Exception {
        return new MongoTemplate(primaryFactory(this.mongoProperties));
    }

    @Primary
    @Bean
    public MongoDbFactory primaryFactory(MongoProperties mongoProperties) throws Exception {
        ServerAddress serverAdress = new ServerAddress(mongoProperties.getUri());
        //不存在用户名于密码的情况
        if(mongoProperties.getUsername()==null && mongoProperties.getPassword()==null){
            return new SimpleMongoDbFactory(new MongoClient(serverAdress), mongoProperties.getDatabase());
        }else{
            List<MongoCredential> mongoCredentialList = new ArrayList<>();
            mongoCredentialList.add(MongoCredential.createCredential(mongoProperties.getUsername(), mongoProperties.getDatabase(), mongoProperties.getPassword()));
            return new SimpleMongoDbFactory(new MongoClient(serverAdress, mongoCredentialList),  mongoProperties.getDatabase());
        }
    }
}