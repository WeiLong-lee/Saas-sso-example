package com.pig4cloud.pigx.auth.service;


import com.pig4cloud.pigx.auth.domain.SaasUser;
import com.pig4cloud.pigx.auth.mapper.SaasUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
    private PasswordEncoder passwordEncoder;

    @Cacheable(value = "auth-sso",key = "#userName")
    public int saveUser(String userName,String password){
       return saasUserMapper.insert(SaasUser.builder().userName(userName).password(passwordEncoder.encode(password)).build());
    }
}
