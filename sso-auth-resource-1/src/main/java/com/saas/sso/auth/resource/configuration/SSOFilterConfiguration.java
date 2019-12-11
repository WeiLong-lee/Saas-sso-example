package com.saas.sso.auth.resource.configuration;

import com.saas.sso.auth.resource.security.filter.SSOCodeCheckFilter;
import com.saas.sso.auth.resource.security.filter.SSOTokenCheckFilter;
import com.saas.sso.auth.resource.security.token.SSOTokenInspector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * 过滤器配置
 *
 * @author feng
 * @date 2019/12/11
 */
@Configuration
public class SSOFilterConfiguration {

    /**
     * 允许跨域的源ip
     */
    @Value("#{'${custom.properties.allow-origins}'.split(',')}")
    private List<String> allowOrigins;

    /**
     * 跨域Filter
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> SSOCorsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        for (String allowOrigin : allowOrigins) {
            config.addAllowedOrigin(allowOrigin);
        }
        config.addAllowedHeader(CorsConfiguration.ALL);
        config.addAllowedMethod(CorsConfiguration.ALL);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    /**
     * SSOCode校验Filter
     */
    @Bean
    public FilterRegistrationBean<SSOCodeCheckFilter> SSOCodeCheckFilter() {
        FilterRegistrationBean<SSOCodeCheckFilter> filter = new FilterRegistrationBean<>();
        filter.setName("SSOCodeFilter");
        filter.setFilter(new SSOCodeCheckFilter());
        filter.setOrder(10);
        return filter;
    }

    /**
     * token校验Filter
     */
    @Bean
    public FilterRegistrationBean<SSOTokenCheckFilter> SSOTokenCheckFilter(SSOTokenInspector tokenInspector) {
        FilterRegistrationBean<SSOTokenCheckFilter> filter = new FilterRegistrationBean<>();
        filter.setName("SSOTokenCheckFilter");
        filter.setFilter(new SSOTokenCheckFilter(tokenInspector));
        filter.setOrder(20);
        return filter;
    }

}
