package com.saas.sso.auth.server.handler;

import com.alibaba.fastjson.JSONObject;
import com.saas.sso.auth.server.domain.auth.log.AuthenticationSuccessLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;

/**
 * 登录成功Handler
 *
 * @author feng
 * @date 2019/12/02
 */
@Slf4j(topic = "sso_auth_log")
public class SaasSSOAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        WebAuthenticationDetails details = authentication.getDetails() != null ? (WebAuthenticationDetails) authentication.getDetails() : null;

        String username = authentication.getName();
        String clientId = "null";
        String remoteAddress = (details == null ? "null" : details.getRemoteAddress());
        String oldSessionId = (details == null ? "null" : details.getSessionId());
        String newSessionId = (session == null ? "null" : session.getId());
        String msg = "Authentication success,user logged in";

        if (session != null) {
            DefaultSavedRequest defaultSavedRequest = (DefaultSavedRequest) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
            if (defaultSavedRequest != null) {
                String[] values = defaultSavedRequest.getParameterValues("client_id");
                if (values != null) {
                    clientId = (values.length == 1) ? values[0] : Arrays.toString(values);
                }
            } else {
                msg = "Authentication success,no savedRequest for logged-in user";
            }
        } else {
            msg = "Authentication success,no session exists for logged-in user";
        }

        log.info(JSONObject.toJSONString(new AuthenticationSuccessLog(username, clientId, remoteAddress, oldSessionId, newSessionId, msg)));
        super.onAuthenticationSuccess(request, response, authentication);
    }

}
