package com.webchat.common.enums;

import lombok.Getter;

@Getter
public enum RoleCodeEnum {

    USER(1, "普通用户"),

    ADMIN(2, "管理员"),

    BLACK(3, "黑名单");

    private Integer code;
    private String name;

    RoleCodeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
