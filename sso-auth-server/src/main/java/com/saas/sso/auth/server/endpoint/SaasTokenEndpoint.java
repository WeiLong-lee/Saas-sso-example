package com.saas.sso.auth.server.endpoint;

import com.saas.sso.auth.server.domain.R;
import com.saas.sso.auth.server.service.SaasUserService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @Description: token 管理
 * @Author: Waylon
 * @Date: 2019/10/23
 */
@RestController
@AllArgsConstructor
@RequestMapping("/token")
public class SaasTokenEndpoint {


    private final TokenStore tokenStore;
    private final RedisTemplate redisTemplate;
//    private final CacheManager cacheManager;

    private SaasUserService saasUserService;

    @GetMapping("/register")
    public ModelAndView register() {
        return new ModelAndView("ftl/register");
    }

    @PostMapping("/register/user")
    public R registerUser(@RequestParam(value = "username",required = true) String userName,
                                     @RequestParam(value = "password",required = true) String password) {

        int result = saasUserService.saveUser(userName,password);
        if(result == 0){
            R.builder().code(1) .data(Boolean.FALSE).msg("注册失败").build();
        }
        return R.builder().code(0) .data(Boolean.TRUE).msg("注册成功").build();
    }

    /**
     * 认证页面
     *
     * @return ModelAndView
     */
    @GetMapping("/login")
    public ModelAndView require() {
        return new ModelAndView("ftl/login");
    }

    @GetMapping("/user")
    @ResponseBody
    public String getCurrentUser(){
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

    @GetMapping("/oauth/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        new SecurityContextLogoutHandler().logout(request, null, null);
        response.sendRedirect(request.getHeader("referer"));
    }

    /**
     * 退出token
     *
     * @param authHeader Authorization
     */
    @DeleteMapping("/logout")
    public R logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        if (StringUtils.isBlank(authHeader)) {
            return R.builder()
                    .code(1)
                    .data(Boolean.FALSE)
                    .msg("退出失败，token 为空").build();
        }

        String tokenValue = authHeader.replace("Bearer", "").trim();
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
        if (accessToken == null || StringUtils.isBlank(accessToken.getValue())) {
            return R.builder()
                    .code(1)
                    .data(Boolean.FALSE)
                    .msg("退出失败，token 无效").build();
        }

        OAuth2Authentication auth2Authentication = tokenStore.readAuthentication(accessToken);
//        cacheManager.getCache("user_details")
//                .evict(auth2Authentication.getName());
        tokenStore.removeAccessToken(accessToken);
        return new R<>(Boolean.TRUE);
    }

    /**
     * 令牌管理调用
     *
     * @param token token
     * @return
     */
    @DeleteMapping("/{token}")
    public R<Boolean> delToken(@PathVariable("token") String token) {
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
        tokenStore.removeAccessToken(oAuth2AccessToken);
        return new R<>();
    }




}
