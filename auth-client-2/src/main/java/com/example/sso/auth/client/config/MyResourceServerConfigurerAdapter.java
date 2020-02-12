package com.example.sso.auth.client.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * 释放资源配置
 * @Author: Feng
 * @Date: 2019/11/15
 */
@Configuration
public class MyResourceServerConfigurerAdapter extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/info").permitAll()
                .anyRequest().authenticated();
    }

}