package com.hcc.config;

import com.hcc.interceptor.ClinicInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 表明这是一个配置类
public class WebMVCInterceptor implements WebMvcConfigurer { //定义配置
    @Override
    public void addInterceptors(InterceptorRegistry registry) { // 添加拦截器
        registry.addInterceptor(new ClinicInterceptor()).addPathPatterns("/");
    }
}
