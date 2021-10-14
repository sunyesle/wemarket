package com.sys.market.advice.exception;

// 이미 동일한 사용자가 존재할 때
public class CUserExistsException extends RuntimeException {
    private static final long serialVersionUID = -5935731668071738596L;

    public CUserExistsException() {
    }

    public CUserExistsException(String message) {
        super(message);
    }

    public CUserExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
