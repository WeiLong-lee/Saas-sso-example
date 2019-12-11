package com.saas.sso.auth.resource.security.filter;

import com.saas.sso.auth.resource.security.token.SSOTokenInspector;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token校验Filter
 *
 * @author feng
 * @date 2019/12/11
 */
@Slf4j
public class SSOTokenCheckFilter implements Filter {

    private SSOTokenInspector tokenInspector;

    public SSOTokenCheckFilter(SSOTokenInspector tokenInspector) {
        this.tokenInspector = tokenInspector;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        boolean validToken = false;

        validToken = tokenInspector.inspect(request);
        // TODO
        // try {
        //     validToken = tokenInspector.inspect(request);
        // } catch () {
        //
        // }
        if (validToken) {
            chain.doFilter(req, res);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    public void destroy() {

    }
}
