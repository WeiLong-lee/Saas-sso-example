package com.example.sso.auth.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@EnableOAuth2Sso
@SpringBootApplication
public class AuthClient1Application {

    public static void main(String[] args) {
        SpringApplication.run(AuthClient1Application.class, args);
    }



}
