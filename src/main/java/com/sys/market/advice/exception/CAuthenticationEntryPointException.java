package com.sys.market.advice.exception;

public class CAuthenticationEntryPointException extends RuntimeException {
    private static final long serialVersionUID = 2615504285888223962L;

    public CAuthenticationEntryPointException() {
    }

    public CAuthenticationEntryPointException(String message) {
        super(message);
    }

    public CAuthenticationEntryPointException(String message, Throwable cause) {
        super(message, cause);
    }
}
