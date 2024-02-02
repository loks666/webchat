package com.webchat.common.exception;

import com.webchat.common.enums.APIErrorCommonEnum;

public class BusinessException extends RuntimeException {

    private Integer code = 500;

    public BusinessException() {
        super();
    }

    public BusinessException(String msg) {
        super(msg);
    }

    public BusinessException(APIErrorCommonEnum apiErrorCommonEnum) {
        super(apiErrorCommonEnum.getMessage());
        this.code = apiErrorCommonEnum.getCode();
    }

    public BusinessException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public Integer getCode() {
        return code;
    }
}
