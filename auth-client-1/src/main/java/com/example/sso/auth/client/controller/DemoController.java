
package com.example.sso.auth.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/***
* @Description:
* @Author: Waylon
* @Date: 2019/11/7
*/
@RestController
public class DemoController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/user")
    public Authentication user(Authentication authentication) {
        return authentication;
    }

    @GetMapping("/info")
    public String info(){
        return "auth client1 info";
    }

    @GetMapping("/info-client2")
    public String infoClient2(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,authHeader);
        String str = restTemplate.postForObject("http://sso-tmall:10082/client2/info",new HttpEntity<String>(headers),String.class);
        return str;
    }
    @PostMapping("/exit")
    public String logout(HttpServletRequest request, HttpServletResponse response){

        String result = null;




        return result;
    }



}
