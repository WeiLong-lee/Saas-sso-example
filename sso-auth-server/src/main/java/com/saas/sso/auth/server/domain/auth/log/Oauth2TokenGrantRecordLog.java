package com.saas.sso.auth.server.domain.auth.log;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.Set;

/**
 * Oauth2Token发放记录日志
 *
 * @author feng
 * @date 2019/12/06
 */
@Getter
@Setter
@ToString
public class Oauth2TokenGrantRecordLog {

    public Oauth2TokenGrantRecordLog(
            String username, String clientId,
            String accessTokenValue, Date accessTokenExpiration, Number accessTokenExpireInSeconds,
            String refreshTokenValue, Date refreshTokenExpiration,
            Set<String> tokenScope
    ) {
        this.username = username;
        this.clientId = clientId;
        this.accessTokenValue = accessTokenValue;
        this.accessTokenExpiration = accessTokenExpiration;
        this.accessTokenExpireInSeconds = accessTokenExpireInSeconds;
        this.refreshTokenValue = refreshTokenValue;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.tokenScope = tokenScope;
    }

    private String msg = "Grant and store token for user";

    private String uri = "/oauth/token";

    private String username;

    private String clientId;

    private String accessTokenValue;

    private Date accessTokenExpiration;

    private Number accessTokenExpireInSeconds;

    private String refreshTokenValue;

    private Date refreshTokenExpiration;

    private Set<String> tokenScope;

}
