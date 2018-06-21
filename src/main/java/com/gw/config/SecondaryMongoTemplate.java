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
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;

/**
 * 第二数据源
 */
@Configuration  //Configuration
@EnableAutoConfiguration
@EnableMongoRepositories(basePackages = "com.gw.config.secondary", mongoTemplateRef = "secondaryMongo")
public class SecondaryMongoTemplate {
    @Autowired
    @Qualifier("secondaryMongoProperties")
    private MongoProperties mongoProperties;

    /**
     * 注意：Bean 的名称与方法名称不要一样，如果一样启动的时候会出错，导致无法启动程序
     * @return
     * @throws Exception
     */
    @Bean(name = "secondaryMongo")
    public MongoTemplate secondaryMongoTemplate() throws Exception {
        return new MongoTemplate(secondaryFactory(this.mongoProperties));
    }

    @Bean
    public MongoDbFactory secondaryFactory(MongoProperties mongoProperties) throws Exception {
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