package com.gw.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpStatus;
/**
 * Created by chenjin on 2018/6/2.
 */
@Configuration
@EnableSpringDataWebSupport
//public class ErrorPageConfig extends WebMvcConfigurerAdapter implements EmbeddedServletContainerCustomizer { //1.5.9版本
public class ErrorPageConfig implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

   /* @Override //设置自定义拦截器
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
    }*/

    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
        factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,"/e/404"));
        factory.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,"/e/500"));
        factory.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST,"/e/400"));
    }
}
