package com.webchat.config.annotation;


import java.lang.annotation.*;

/**
 * @Author: by loks666 on GitHub
 * @Date: 26.11.21 11:56 下午
 * 自定义注解实现:校验是否登录
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ValidateLogin {

}
