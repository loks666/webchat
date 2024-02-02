package com.webchat.common.enums;

import lombok.Getter;

@Getter
public enum UserStatusEnum {

    ENABLE(1),

    DISABLE(0);

    private Integer status;

    UserStatusEnum(Integer status) {
        this.status = status;
    }
}
