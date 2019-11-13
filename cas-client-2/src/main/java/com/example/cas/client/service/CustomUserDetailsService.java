package com.example.cas.client.service;


import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.*;


/**
 * @Description:
 * @Author: Waylon
 * @Date: 2019/10/28
 */
public class CustomUserDetailsService implements UserDetailsService, AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("当前的用户名是："+username);

        return new User(username,"admin",AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN"));

    }

    @Override
    public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException {
        System.out.println("当前的用户名是："+token.getName());

        return new User("admin","admin",AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN"));

    }
}
