package com.webchat.common.exception;

import com.webchat.common.enums.APIErrorCommonEnum;

public class NotFoundException extends RuntimeException {

    private Integer code = 404;

    public NotFoundException() {
        super();
    }

    public NotFoundException(String msg) {
        super(msg);
    }

    public NotFoundException(APIErrorCommonEnum apiErrorCommonEnum) {
        super(apiErrorCommonEnum.getMessage());
        this.code = apiErrorCommonEnum.getCode();
    }

    public NotFoundException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public Integer getCode() {
        return code;
    }
}
