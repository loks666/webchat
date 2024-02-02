package com.webchat.common.exception;

import com.webchat.common.enums.APIErrorCommonEnum;

public class ParamException extends RuntimeException {

    private Integer code = 444;

    public ParamException() {
        super();
    }

    public ParamException(String msg) {
        super(msg);
    }

    public ParamException(APIErrorCommonEnum apiErrorCommonEnum) {
        super(apiErrorCommonEnum.getMessage());
        this.code = apiErrorCommonEnum.getCode();
    }

    public ParamException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public ParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public Integer getCode() {
        return code;
    }
}
