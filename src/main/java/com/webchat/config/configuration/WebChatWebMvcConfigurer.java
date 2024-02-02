package com.webchat.config.configuration;

import com.webchat.config.annotation.ValidateLoginInterceptor;
import com.webchat.config.annotation.ValidatePermissionInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/***
 * Web 拦截器配置
 **/
@Configuration
public class WebChatWebMvcConfigurer implements WebMvcConfigurer {

    /***
     * 注册自定义拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /**
         * 注册权限控制拦截器
         */
        registry.addInterceptor(validatePermissionInterceptor()).addPathPatterns("/api/**");
        /**
         * 注册登录核验拦截器
         */
        registry.addInterceptor(validateLoginInterceptor()).addPathPatterns("/api/**");
    }

    /***
     * 登录核验拦截器
     * @return
     */
    @Bean
    public ValidateLoginInterceptor validateLoginInterceptor() {
        return new ValidateLoginInterceptor();
    }
    /***
     * 权限控制拦截器
     * @return
     */
    @Bean
    public ValidatePermissionInterceptor validatePermissionInterceptor() {
        return new ValidatePermissionInterceptor();
    }
}