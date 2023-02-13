package com.acme.acmemall.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/13 10:13
 */
@Configuration
public class ConfigAdaper extends WebMvcConfigurerAdapter {
    @Autowired
    LoginUserHandlerMethodArgumentResolver loginUserHandlerMethodArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(loginUserHandlerMethodArgumentResolver);
    }
}
