package com.saas.sso.auth.resource.controller;

import com.alibaba.fastjson.JSONObject;
import com.saas.sso.auth.resource.pojo.R;
import com.saas.sso.auth.resource.security.constant.SecurityConstants;
import com.saas.sso.auth.resource.security.crsf.CRSFUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * For SSO Code Check
 *
 * @author feng
 * @date 2019/12/2019/12/10
 */
@Slf4j
@RestController
public class SSOLoginController {

    @RequestMapping(value = SecurityConstants.SSO_GET_CRSF_TOKEN_URI)
    public String ssoCodeCheck(HttpServletRequest request, HttpServletResponse response) {
        String originalToken = CRSFUtils.generateOriginalCRSFToken();
        String salt = CRSFUtils.generateCRSFTokenSalt();
        String CRSFToken = CRSFUtils.generateCRSFToken(originalToken, salt);

        request.getSession(false).setAttribute(SecurityConstants.ORIGINAL_SSO_CRSF_TOKEN_NAME, originalToken);

        Cookie saltCookie = new Cookie(SecurityConstants.SSO_CRSF_TOKEN_SALT_NAME, salt);
        saltCookie.setHttpOnly(true);
        saltCookie.setPath(SecurityConstants.SERVLET_CONTEXT_PATH);
        response.addCookie(saltCookie);

        JSONObject result = new JSONObject();
        result.put("success", true);
        result.put(SecurityConstants.SSO_CRSF_TOKEN_NAME, CRSFToken);
        return result.toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/sso/info")
    public String ssoInfo(HttpServletRequest request, HttpServletResponse response) {
        return "{\"info\":\"SSO-INFO\"}";
    }

    /**
     * 退出登录
     *
     * @param request
     * @param token
     */
    @GetMapping("/exit")
    public R<Boolean> exit(HttpServletRequest request, String token) {
        try {
            HttpSession session = request.getSession(false);
            if (null != session) {
                session.invalidate();
            } else {
                log.info("null session logout,token:{}", token);
            }
            return new R<>(Boolean.TRUE);
        } catch (Exception e) {
            log.error("登出错误:{}", e.getMessage());
            return new R<>(Boolean.FALSE).setMsg(e.getMessage());
        }
    }

}
