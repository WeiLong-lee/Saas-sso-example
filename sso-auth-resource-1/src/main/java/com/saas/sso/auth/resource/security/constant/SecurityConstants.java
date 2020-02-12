package com.saas.sso.auth.resource.security.constant;

/**
 * @author feng
 * @date 2019/12/12
 */
public class SecurityConstants {

    public static final String ORIGINAL_SSO_CRSF_TOKEN_NAME = "OriginalCRSFToken";

    public static final String SSO_CRSF_TOKEN_NAME = "CRSFToken";

    public static final String SSO_CRSF_TOKEN_SALT_NAME = "CRSFTokenSalt";

    public static final String SERVLET_CONTEXT_PATH = "/resource";

    public static final String SSO_GET_CRSF_TOKEN_URI = "/getCRSFToken";

    public static final String SSO_GET_CRSF_TOKEN_FULL_URI = SecurityConstants.SERVLET_CONTEXT_PATH + SecurityConstants.SSO_GET_CRSF_TOKEN_URI;

    public static final String SSO_LOGOUT_URI = "/exit";

    public static final String SSO_LOGOUT_URI_FULL = SecurityConstants.SERVLET_CONTEXT_PATH + SecurityConstants.SSO_LOGOUT_URI;

}
