package com.saas.sso.auth.server.service;

import com.saas.sso.auth.server.domain.SaasUser;
import com.saas.sso.auth.server.mapper.SaasUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: Waylon
 * @Date: 2019/10/23
 */
@Service
public class SaasUserService {

    @Autowired
    private SaasUserMapper saasUserMapper;

    @Autowired
    private PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Cacheable(value = "auth-sso",key = "#userName")
    public int saveUser(String userName,String password){
       return saasUserMapper.insert(SaasUser.builder().userName(userName).password(ENCODER.encode(password)).build());
    }
}
