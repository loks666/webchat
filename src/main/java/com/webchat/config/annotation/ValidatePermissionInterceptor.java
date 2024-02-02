package com.webchat.config.annotation;

import com.webchat.common.enums.RoleCodeEnum;
import com.webchat.common.exception.AuthException;
import com.webchat.common.helper.SessionHelper;
import com.webchat.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Author: by loks666 on GitHub
 * @Date: 27.11.21 12:07 上午
 */
public class ValidatePermissionInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RoleService roleService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 获取拦截到的方法
        Method method = handlerMethod.getMethod();
        // 获取方法到的RequestPermission注解
        ValidatePermission requestPermission = method.getAnnotation(ValidatePermission.class);
        if (requestPermission == null) {
            // 未获取到权控注解，说明不需要拦截，直接放过
            return true;
        }
        /***
         * 权限校验
         */
        if (requestPermission.role().length > 0) {
            RoleCodeEnum[] roleEnums = requestPermission.role();
            // 获取当前登录的用户ID
            String userId = SessionHelper.getCurrentUserId();
            if (StringUtils.isBlank(userId)) {
                // 未登录！权限校验失败
                throw new AuthException("未登录！");
            }
            // 一个一个校验，或者的关系：即满足任一权限则通过
            for (RoleCodeEnum roleEnum : roleEnums) {
                // 用户任一权限校验通过，则认为拥有操作权限
                if (roleService.hasRole(userId, roleEnum.getCode())) {
                    return true;
                }
            }
        }
        throw new AuthException("无权限!");
    }
}
