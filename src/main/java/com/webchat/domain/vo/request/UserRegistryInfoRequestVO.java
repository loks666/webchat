package com.webchat.domain.vo.request;

import lombok.Data;

/***
 * 用户注册请求信息
 */
@Data
public class UserRegistryInfoRequestVO {

    private String photo;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 密码
     */
    private String password;

    /***
     * 数字验证码
     */
    private String picCheckCode;
}
