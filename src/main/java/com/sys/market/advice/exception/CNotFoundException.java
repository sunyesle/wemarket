package com.sys.market.advice.exception;

public class CNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 2377661990937495263L;

    public CNotFoundException() {
    }

    public CNotFoundException(String message) {
        super(message);
    }

    public CNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
