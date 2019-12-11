package com.saas.sso.auth.resource.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * For SSO Code Check
 *
 * @author feng
 * @date 2019/12/2019/12/10
 */
@RestController
public class SSOCheckController {

    @RequestMapping(value = "/sso/check")
    public boolean ssoCodeCheck(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/sso/info")
    public String ssoInfo(HttpServletRequest request, HttpServletResponse response) {
        return "ssoInfo";
    }
}
