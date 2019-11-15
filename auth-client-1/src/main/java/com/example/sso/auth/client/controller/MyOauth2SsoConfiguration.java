package com.example.sso.auth.client.controller;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoDefaultConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * 释放资源配置
 * @Author: Feng
 * @Date: 2019/11/15
 */
@Order(50)
@Configuration
public class MyOauth2SsoConfiguration extends OAuth2SsoDefaultConfiguration {

    public MyOauth2SsoConfiguration(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/info").permitAll();
        super.configure(http);
    }


}
