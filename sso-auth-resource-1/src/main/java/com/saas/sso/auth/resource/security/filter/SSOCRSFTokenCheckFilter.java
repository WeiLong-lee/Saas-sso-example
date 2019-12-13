package com.saas.sso.auth.resource.security.filter;

import com.saas.sso.auth.resource.security.constant.SecurityConstants;
import com.saas.sso.auth.resource.security.crsf.CRSFUtils;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * CRSF Token校验Filter
 *
 * @author feng
 * @date 2019/12/12
 */
public class SSOCRSFTokenCheckFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        String uri = request.getRequestURI();
        boolean validCRSFToken = false;

        // if (SecurityConstants.SSO_GET_CRSF_TOKEN_FULL_URI.equals(uri)) {
        //     validCRSFToken = true;
        // }
        if (StringUtils.isEmpty(session.getAttribute(SecurityConstants.ORIGINAL_SSO_CRSF_TOKEN_NAME))) {
            validCRSFToken = true;
        } else {
            String original = (String) session.getAttribute(SecurityConstants.ORIGINAL_SSO_CRSF_TOKEN_NAME);
            String CRSFToken = request.getHeader(SecurityConstants.SSO_CRSF_TOKEN_NAME);

            if(!StringUtils.isEmpty(CRSFToken)) {
                Cookie[] cookies = request.getCookies();
                for (Cookie cookie : cookies) {
                    if(cookie.getName().equals(SecurityConstants.SSO_CRSF_TOKEN_SALT_NAME)) {
                        String salt = cookie.getValue();
                        if(!StringUtils.isEmpty(salt)) {
                            validCRSFToken = CRSFUtils.checkCRSFToken(original, salt, CRSFToken);
                            break;
                        }
                    }
                }
            }
        }

        if (validCRSFToken) {
            chain.doFilter(req, res);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    public void destroy() {

    }

}
