package com.pig4cloud.pigx.auth.service;


import com.pig4cloud.pigx.auth.mapper.SaasUserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Description:
 * @Author: Waylon
 * @Date: 2019/10/23
 */
@Log4j2
@Service
@AllArgsConstructor
public class SaasUserDetailServiceImpl implements SaasUserDetailService{


    private final SaasUserMapper saasUserMapper;
    private final CacheManager cacheManager;

    @Override
    public UserDetails loadUserBySocial(String code) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Cache cache = cacheManager.getCache("user_details");
        if (cache != null && cache.get(username) != null) {
            return (PigxUser) cache.get(username).get();
        }
       UserDetails userDetails = Optional.of(saasUserMapper.selectByUserName(username)).map(saasUser-> new User(saasUser.getUserName(),
                saasUser.getPassword(),
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN")
               )).orElseThrow(()-> new UsernameNotFoundException("User" +username + " was not found in the database"));
        return userDetails;
    }
}
