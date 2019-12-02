package com.saas.sso.auth.server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@Slf4j
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
        redisTemplate.execute((RedisCallback<Long>) connection -> {
            connection.set(codeKey(code).getBytes(), SerializationUtils.serialize(authentication),
                    Expiration.from(15, TimeUnit.SECONDS), RedisStringCommands.SetOption.UPSERT);
            return 1L;
        });
        log.debug("Store Authorization_Code:'{}' for user:'{}' from client:'{}'", code, authentication.getName(), authentication.getOAuth2Request().getClientId());
    }

    @Override
    protected OAuth2Authentication remove(final String code) {
        OAuth2Authentication oAuth2Authentication = redisTemplate.execute((RedisCallback<OAuth2Authentication>) connection -> {
            byte[] keyByte = codeKey(code).getBytes();
            byte[] valueByte = connection.get(keyByte);
            if (valueByte != null) {
                connection.del(keyByte);
                return (OAuth2Authentication) SerializationUtils.deserialize(valueByte);
            }
            return null;
        });
        return oAuth2Authentication;
    }

    private String codeKey(String code) {
        return prefix + ":" + code;
    }

}
