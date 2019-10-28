package com.saas.sso.auth.server.service;


import com.saas.sso.auth.server.constant.SecurityConstants;
import com.saas.sso.auth.server.mapper.SaasUserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public UserDetails loadUserBySocial(String code) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       UserDetails userDetails = Optional.of(saasUserMapper.selectByUserName(username)).map(saasUser-> new User(saasUser.getUserName(),
                SecurityConstants.BCRYPT + saasUser.getPassword(),
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN")
               )).orElseThrow(()-> new UsernameNotFoundException("User" +username + " was not found in the database"));
        return userDetails;
    }
}
