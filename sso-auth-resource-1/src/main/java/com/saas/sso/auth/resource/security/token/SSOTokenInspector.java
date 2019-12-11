package com.saas.sso.auth.resource.security.token;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * token检查器
 *
 * @author feng
 * @date 2019/12/11
 */
@Slf4j
@Component
public class SSOTokenInspector {

    @Autowired
    private SSORemoteTokenCheckService tokenService;

    private SSOTokenExtractor extractor = new SSOTokenExtractor();

    public boolean inspect(HttpServletRequest request) {
        SSOToken token = extractor.extractToken(request);
        if (null != token) {
            return inspect(token);
        }

        return false;
    }

    public boolean inspect(SSOToken token) {
        Map<String, Object> authentication = tokenService.loadAuthentication(token.getAccessTokenValue());
        log.debug("Valid SSOToken:{},load authentication:'{}' from remote auth server", token, authentication);

        // TODO
        return true;
    }

}
