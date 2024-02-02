package com.webchat.config.annotation;

import com.webchat.common.enums.RoleCodeEnum;

import java.lang.annotation.*;

/**
 * 自定义注解实现:校验是否管理员
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ValidatePermission {

    /***
     * 需要进行校验的权限类型，默认校验管理员
     * @see RoleCodeEnum
     * @return
     */
    RoleCodeEnum[] role() default {RoleCodeEnum.ADMIN};
}
