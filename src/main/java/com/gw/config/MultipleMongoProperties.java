package com.gw.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 多数据源配置属性累
 */
@Configuration
@EnableAutoConfiguration
public class MultipleMongoProperties {
    @Bean(name="primaryMongoProperties")
    @Primary
    @ConfigurationProperties(prefix="spring.data.mongodb.primary")
    public MongoProperties primaryMongoProperties() {
        System.out.println("-------------------- primaryMongoProperties init ---------------------");
        return new MongoProperties();
    }

    @Bean(name="secondaryMongoProperties")
    @ConfigurationProperties(prefix="spring.data.mongodb.secondary")
    public MongoProperties secondaryMongoProperties() {
        System.out.println("-------------------- secondaryMongoProperties init ---------------------");
        return new MongoProperties();
    }

}