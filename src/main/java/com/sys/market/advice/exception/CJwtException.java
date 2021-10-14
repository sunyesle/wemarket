package com.sys.market.advice.exception;

public class CJwtException extends RuntimeException {
    private static final long serialVersionUID = 8107113850943511617L;

    public CJwtException() {
    }

    public CJwtException(String message) {
        super(message);
    }

    public CJwtException(String message, Throwable cause) {
        super(message, cause);
    }
}
