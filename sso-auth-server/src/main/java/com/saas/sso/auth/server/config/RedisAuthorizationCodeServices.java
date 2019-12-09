package com.saas.sso.auth.server.config;

import com.alibaba.fastjson.JSONObject;
import com.saas.sso.auth.server.domain.auth.log.Oauth2AuthorizationCodeGrantRecordLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.util.concurrent.TimeUnit;

/**
 * Authorization_Code存取的Redis实现
 *
 * @author feng
 * @date 2019/12/02
 */
@Component
@Slf4j(topic = "sso_auth_log")
public class RedisAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {

    @Value("#{'${oauth2.code.redis.prefix}'}")
    private String prefix;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 存储code
     *
     * @description OAuth2Authentication没有无参构造函数，此处需采用最底层的方式存储，不能使用redisTemplate.opsForValue().get(key)
     */
    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        Boolean setResult = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.set(RedisAuthorizationCodeServices.this.codeKey(code).getBytes(), SerializationUtils.serialize(authentication),
                        Expiration.from(15, TimeUnit.SECONDS), RedisStringCommands.SetOption.UPSERT);
            }
        });

        if (setResult == null || !setResult) {

            log.error("{\"message\":\"Redis Set Operation failure : Can't Store Authorization_Code:'{}' for user:'{}' from client:'{}' into Redis\"}", code, authentication.getName(), authentication.getOAuth2Request().getClientId());
            throw new RuntimeException("Redis Set Operation failure : Can't Store Authorization_Code into Redis");
        } else {
            log.info(JSONObject.toJSONString(new Oauth2AuthorizationCodeGrantRecordLog(authentication.getName(), authentication.getOAuth2Request().getClientId(), code)));
            // log.info("{\"message\":\"Store Authorization_Code:'{}' for user:'{}' from client:'{}' into Redis\"}", code, authentication.getName(), authentication.getOAuth2Request().getClientId());
        }
    }

    @Override
    protected OAuth2Authentication remove(final String code) {
        OAuth2Authentication oAuth2Authentication = redisTemplate.execute(new RedisCallback<OAuth2Authentication>() {
            @Override
            public OAuth2Authentication doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] keyByte = RedisAuthorizationCodeServices.this.codeKey(code).getBytes();
                byte[] valueByte = connection.get(keyByte);
                if (valueByte != null) {
                    OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) SerializationUtils.deserialize(valueByte);
                    connection.del(keyByte);
                    return oAuth2Authentication;
                } else {
                    log.error("{\"message\":\"No OAuth2Authentication info found in Redis for Authorization_Code:{}\"}", code);
                    return null;
                }
            }
        });
        return oAuth2Authentication;
    }

    private String codeKey(String code) {
        return prefix + ":" + code;
    }

}
