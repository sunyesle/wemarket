package com.sys.market.advice.exception;

public class CInvalidImageFormatException extends RuntimeException {
    private static final long serialVersionUID = -8700640461286187692L;

    public CInvalidImageFormatException() {
    }

    public CInvalidImageFormatException(String message) {
        super(message);
    }

    public CInvalidImageFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
