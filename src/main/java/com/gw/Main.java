package com.gw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * 入口
 * @SpringBootApplication开启了Spring的组件扫描和springboot的自动配置功能，相当于将以下三个注解组合在了一起
 *（1）@Configuration：表名该类使用基于Java的配置,将此类作为配置类
 *（2）@ComponentScan：启用注解扫描
 *（3）@EnableAutoConfiguration：开启springboot的自动配置功能
 * @SpringBootApplication(exclude = MongoAutoConfiguration.class) ,这个注解可以禁用springboot自带的MongoDB配置。
 */
@SpringBootApplication
//@ImportResource({"classpath:context.xml"})
//@EnableAutoConfiguration
//@ComponentScan
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    /*@Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        tomcat.setUriEncoding(Charset.forName("UTF-8"));
        tomcat.setContextPath("/api");
        tomcat.setPort(8088);
        tomcat.addConnectorCustomizers(new MyTomcatConnectorCustomizer());
        return tomcat;
    }

    class MyTomcatConnectorCustomizer implements TomcatConnectorCustomizer
    {
        public void customize(Connector connector)
        {
            Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
            //设置最大连接数
            protocol.setMaxConnections(2000);
            //设置最大线程数
            protocol.setMaxThreads(2000);
            //protocol.setConnectionTimeout(30000);
        }
    }*/
}