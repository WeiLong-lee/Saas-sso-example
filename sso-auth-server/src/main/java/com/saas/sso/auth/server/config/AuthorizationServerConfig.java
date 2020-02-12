package com.saas.sso.auth.server.config;

import com.alibaba.fastjson.JSONObject;
import com.saas.sso.auth.server.constant.SecurityConstants;
import com.saas.sso.auth.server.domain.auth.log.Oauth2TokenGrantRecordLog;
import com.saas.sso.auth.server.service.SaasClientDetailsService;
import com.saas.sso.auth.server.service.SaasUserDetailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 认证服务器配置
 * @Author: Waylon
 * @Date: 2019/10/23
 */
@Order(1)
@Slf4j(topic = "sso_auth_log")
@Configuration
@AllArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final DataSource dataSource;
    private final SaasUserDetailService saasUserDetailService;
    private final AuthenticationManager authenticationManager;
    private final RedisConnectionFactory redisConnectionFactory;
    private final RedisAuthorizationCodeServices redisAuthorizationCodeServices;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        SaasClientDetailsService clientDetailsService = new SaasClientDetailsService(dataSource);
        clientDetailsService.setSelectClientDetailsSql(SecurityConstants.DEFAULT_SELECT_STATEMENT);
        clientDetailsService.setFindClientDetailsSql(SecurityConstants.DEFAULT_FIND_STATEMENT);
        clients.withClientDetails(clientDetailsService);

    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
                .allowFormAuthenticationForClients()// 允许录入client_id和client_secret的形式
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancer())
                .userDetailsService(saasUserDetailService)
                .authenticationManager(authenticationManager)
                .reuseRefreshTokens(false)

                .authorizationCodeServices(redisAuthorizationCodeServices);
    }

    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
        // todo
        // tokenStore.setSerializationStrategy();
        tokenStore.setPrefix(SecurityConstants.SAAS_PREFIX + SecurityConstants.OAUTH_PREFIX);
        tokenStore.setAuthenticationKeyGenerator(new DefaultAuthenticationKeyGenerator() {
            @Override
            public String extractKey(OAuth2Authentication authentication) {
                return super.extractKey(authentication);
            }
        });
        return tokenStore;
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            final Map<String, Object> additionalInfo = new HashMap<>(8);
            User user = (User) authentication.getUserAuthentication().getPrincipal();
            additionalInfo.put("username", user.getUsername());// todo userId、username加密
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

            OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
            Date refreshTokenExpiration = null;
            if(refreshToken instanceof DefaultExpiringOAuth2RefreshToken) {
                refreshTokenExpiration = ((DefaultExpiringOAuth2RefreshToken)refreshToken).getExpiration();
            }
            Oauth2TokenGrantRecordLog logInfo = new Oauth2TokenGrantRecordLog(
                    user.getUsername(),
                    authentication.getOAuth2Request().getClientId(),
                    accessToken.getValue(),
                    accessToken.getExpiration(),
                    accessToken.getExpiresIn(),
                    accessToken.getRefreshToken().getValue(),
                    refreshTokenExpiration,
                    accessToken.getScope()
            );
            log.info(JSONObject.toJSONString(logInfo));

            return accessToken;
        };
    }



}
