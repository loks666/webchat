package com.webchat.config.annotation;

import com.webchat.common.exception.AuthException;
import com.webchat.common.helper.SessionHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Author: by loks666 on GitHub
 * @Date: 27.11.21 12:07 上午
 */
public class ValidateLoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 获取拦截到的方法
        Method method = handlerMethod.getMethod();
        // 获取方法到的RequestPermission注解
        ValidateLogin validateLogin = method.getAnnotation(ValidateLogin.class);
        if (validateLogin == null) {
            // 未获取到权控注解，说明不需要拦截，直接放过
            return true;
        }
        String userId = SessionHelper.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            // 未登录！权限校验失败
            throw new AuthException("未登录！");
        }
        return true;
    }
}
