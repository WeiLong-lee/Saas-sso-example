package com.saas.sso.auth.resource.security.oauth.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 自定义Token
 *
 * @author feng
 * @date 2019/12/11
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class SSOToken {

    private String accessTokenValue;

}
