package com.sys.market.advice.exception;

// 로그인 실패
public class CSigninFailedException extends RuntimeException {
    private static final long serialVersionUID = 1659330657504640077L;

    public CSigninFailedException() {
    }

    public CSigninFailedException(String message) {
        super(message);
    }

    public CSigninFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
