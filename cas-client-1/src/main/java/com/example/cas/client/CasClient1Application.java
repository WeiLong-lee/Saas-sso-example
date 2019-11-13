package com.example.cas.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class CasClient1Application {

    public static void main(String[] args) {
        SpringApplication.run(CasClient1Application.class, args);
    }

    @GetMapping("/")
    public String index(){
        return "访问首页";
    }
    @RequestMapping("/hello")
    public String hello(){
        return "不验证";
    }
    @PreAuthorize("hasAuthority('TEST')")
    @RequestMapping("/security")
    public String security(){
        return "hello World security";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping("/authorize")
    public String authorize(){
        return "有权限访问";
    }
}
