package com.saas.sso.auth.server.domain.auth.log;

import com.saas.sso.auth.server.constant.SecurityConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 认证成功日志打印对象
 *
 * @author feng
 * @date 2019/12/06
 */
@Getter
@Setter
@ToString
public class AuthenticationSuccessLog {

    public AuthenticationSuccessLog(String username, String clientId, String remoteAddress, String oldSessionId, String newSessionId, String msg) {
        this.username = username;
        this.clientId = clientId;
        this.remoteAddress = remoteAddress;
        this.oldSessionId = oldSessionId;
        this.newSessionId = newSessionId;
        this.msg = msg;
    }

    private String msg;

    /**
     * 登录表单提交的uri
     */
    private String uri = SecurityConstants.LOGIN_PROCESSING_URL;

    private String username;

    private String clientId;

    /**
     * 用户请求的发起地址（ip）
     */
    private String remoteAddress;

    /**
     * 旧sessionID
     *
     * @description login之前的sessionId
     */
    private String oldSessionId;

    /**
     * 新sessionID
     *
     * @description login成功之后的新sessionId，由spring security将login request的旧sessionId刷新而来
     */
    private String newSessionId;

}
