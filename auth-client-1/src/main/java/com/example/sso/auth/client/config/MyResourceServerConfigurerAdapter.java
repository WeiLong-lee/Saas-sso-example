// package com.example.sso.auth.client.config;
//
// import org.springframework.boot.web.servlet.FilterRegistrationBean;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.Ordered;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// import org.springframework.web.filter.CorsFilter;
//
// /**
//  * Resource Server释放资源配置
//  * @Author: Feng
//  * @Date: 2019/11/15
//  */
// @Configuration
// public class MyResourceServerConfigurerAdapter extends ResourceServerConfigurerAdapter {
//
//     @Override
//     public void configure(HttpSecurity http) throws Exception {
//         http
//                 .authorizeRequests()
//                 .antMatchers("/info").permitAll()
//                 .anyRequest().authenticated();
//     }
//
//     @Bean
//     public FilterRegistrationBean oauthCorsFilter() {
//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         CorsConfiguration config = new CorsConfiguration();
//         config.setAllowCredentials(true);
//
//         config.addAllowedOrigin("http://localhost:3000");
//
//         config.addAllowedHeader(CorsConfiguration.ALL);
//         config.addAllowedMethod(CorsConfiguration.ALL);
//         source.registerCorsConfiguration("/**", config);
//         FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
//         bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//         return bean;
//     }
//
// }