package com.sys.market.advice.exception;

public class CAccessDeniedException extends RuntimeException {
    private static final long serialVersionUID = -408791441387400659L;

    public CAccessDeniedException() {
    }

    public CAccessDeniedException(String message) {
        super(message);
    }

    public CAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
