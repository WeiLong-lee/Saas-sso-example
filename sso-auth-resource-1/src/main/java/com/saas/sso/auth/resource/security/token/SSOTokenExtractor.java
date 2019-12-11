package com.saas.sso.auth.resource.security.token;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * token提取器
 *
 * @author feng
 * @date 2019/12/11
 */
@Slf4j
public class SSOTokenExtractor {

    protected SSOToken extractToken(HttpServletRequest request) {
        return extractHeaderToken(request);
    }

    protected SSOToken extractHeaderToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders("Authorization");
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if ((value.startsWith("Bearer"))) {
                String accessTokenValue = value.substring("Bearer".length()).trim();
                request.setAttribute("access_token_type", "Bearer");
                return new SSOToken(accessTokenValue);
            }
        }
        return null;
    }

}
