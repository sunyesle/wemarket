package com.sys.market.advice.exception;

public class CBadRequestException extends RuntimeException {
    private static final long serialVersionUID = 7907543908457724524L;

    public CBadRequestException() {
    }

    public CBadRequestException(String message) {
        super(message);
    }

    public CBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
