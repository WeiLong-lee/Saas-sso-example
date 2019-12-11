package com.saas.sso.auth.resource.security.filter;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义SSO Filter
 *
 * @author feng
 * @date 2019/12/10
 */
public class SSOCodeCheckFilter implements Filter {

    private static final String SSO_CODE = "abc123";

    private static final String SSO_CODE_URI = "/resource/sso/check";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        boolean authenticated = false;
        String uri = request.getRequestURI();
        String ssoCode = request.getHeader("ssoCode");

        // 验证session是否合法
        if (request.getSession(false) != null) {
            Boolean ssoCodeAuthenticated = (Boolean) request.getSession(false).getAttribute("SSO_CODE_AUTHENTICATED");
            if (BooleanUtils.isTrue(ssoCodeAuthenticated)) {
                authenticated = true;
            }
        }

        // session不合法时，验证是否申请校验SSO_CODE的请求
        if (!authenticated && SSO_CODE_URI.equals(uri) && !StringUtils.isEmpty(ssoCode)) {
            // TODO 修改SSO_CODE的获取来源
            if (SSO_CODE.equals(ssoCode)) {
                authenticated = true;
                request.getSession(true).setAttribute("SSO_CODE_AUTHENTICATED", true);
                // TODO 清除已消费的SSO_CODE
            }
        }

        if (authenticated) {
            chain.doFilter(req, res);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    public void destroy() {

    }
}
