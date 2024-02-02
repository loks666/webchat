package com.webchat.domain.vo.response;

import lombok.Data;


/***
 * 用户基本信息
 */
@Data
public class UserBaseResponseInfoVO {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 头像
     */
    private String photo;

    /***
     * 性别
     */
    private String sex;

    /**
     * 签名
     */
    private String signature;

    /**
     * 用户名
     */
    private String userName;

    /***
     * 手机号
     */
    private String mobile;

    private Integer roleCode;

    /***
     * 注册时间
     */
    private Long registryTime;

    private String registryTimeStr;
}
