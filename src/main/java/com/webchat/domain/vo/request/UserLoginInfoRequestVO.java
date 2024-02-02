package com.webchat.domain.vo.request;

import lombok.Data;

@Data
public class UserLoginInfoRequestVO {

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 密码
     */
    private String password;
}
