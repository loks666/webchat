package com.webchat.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FriendStatusEnum {

    REFUSE(-1, "拒绝"),

    APPLY(0, "申请待处理"),
    PASS(1, "通过");

    private Integer status;

    private String statusName;
}
