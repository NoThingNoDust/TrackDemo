package com.example.demo.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterFilterRegistrationBean() {
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(corsFilter());
        bean.addUrlPatterns("/*");
        bean.setName("corsFilter");
        HashMap<String, String> map = new HashMap<>();
        bean.setInitParameters(map);
        bean.setEnabled(true);
        bean.setOrder(1);
        return bean;
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter();
    }

}
