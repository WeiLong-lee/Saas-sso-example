package com.saas.sso.auth.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * 启动类
 *
 * @author feng
 * @date 2019/12/2019/12/10
 */
@SpringBootApplication
@ServletComponentScan
public class SSOAuthResourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SSOAuthResourceApplication.class, args);
    }

}
