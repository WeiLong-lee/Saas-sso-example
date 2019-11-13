package com.example.sso.auth.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.client.RestTemplate;

@EnableResourceServer
@SpringBootApplication
public class AuthClient1Application {

    public static void main(String[] args) {
        SpringApplication.run(AuthClient1Application.class, args);
    }


    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
