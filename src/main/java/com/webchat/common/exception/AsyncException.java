package com.webchat.common.exception;

/***
 * 异常处理异常
 */
public class AsyncException extends RuntimeException {

    public AsyncException() {
        super();
    }

    public AsyncException(String s) {
        super(s);
    }

    public AsyncException(String message, Throwable cause) {
        super(message, cause);
    }

    public AsyncException(Throwable cause) {
        super(cause);
    }
}
