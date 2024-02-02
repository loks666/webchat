package com.webchat.domain.vo.response;

import lombok.Data;

/***
 * 用户安全信息 / 用户个人中心等需要
 */
@Data
public class UserSafeResponseInfoVO extends UserBaseResponseInfoVO {

    private String mobile;

    private Long registryTime;

    private String registryTimeStr;
}
