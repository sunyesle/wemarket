package com.sys.market.advice.exception;

public class CConflictException extends RuntimeException {
    private static final long serialVersionUID = -7870009449486550364L;

    public CConflictException() {
    }

    public CConflictException(String message) {
        super(message);
    }

    public CConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
