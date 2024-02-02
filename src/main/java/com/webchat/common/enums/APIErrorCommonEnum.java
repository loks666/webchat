package com.webchat.common.enums;

import lombok.Getter;

/***
 * 通用接口异常CODE
 */
@Getter
public enum APIErrorCommonEnum {

    /****
     * 无权限异常CODE:101XXX
     */
    UN_AUTH(101400, "无权限!"),
    USER_UN_LOGIN(101401, "未登录!"),
    USER_NOT_FOUND(101404, "用户不存在!"),
    USER_UN_AUTH(101405, "无权限!"),
    VALID_EXCEPTION(100001, "参数校验失败!"),
    API_UN_AUTH(101406, "接口调用无权限!"),
    UN_SAFE_EVENT(999999, "识别存在非安全事件!");

    private Integer code;
    private String  message;

    APIErrorCommonEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
