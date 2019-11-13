package com.example.sso.auth.client.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Waylon
 * @Date: 2019/11/07
 */
@RestController
public class DemoController {

    @GetMapping("/user")
    public Authentication user(Authentication authentication) {
        return authentication;
    }

    @RequestMapping("/info")
    public String info(){
        return "auth client2 info";
    }
}
