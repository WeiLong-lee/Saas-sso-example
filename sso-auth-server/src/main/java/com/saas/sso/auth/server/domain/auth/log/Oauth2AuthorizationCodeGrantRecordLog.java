package com.saas.sso.auth.server.domain.auth.log;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Oauth2AuthorizationCode发放记录日志
 *
 * @author feng
 * @date 2019/12/06
 */
@Getter
@Setter
@ToString
public class Oauth2AuthorizationCodeGrantRecordLog {

    public Oauth2AuthorizationCodeGrantRecordLog(String username, String clientId, String authorizationCode) {
        this.username = username;
        this.clientId = clientId;
        this.authorizationCode = authorizationCode;
    }

    private String msg = "Grant and store authorization_code for user";

    private String uri = "/oauth/authorize";

    private String username;

    private String clientId;

    private String authorizationCode;

}
