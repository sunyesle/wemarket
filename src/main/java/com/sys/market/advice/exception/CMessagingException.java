package com.sys.market.advice.exception;

public class CMessagingException extends RuntimeException{
    private static final long serialVersionUID = 5283743903958350921L;

    public CMessagingException() {
    }

    public CMessagingException(String message) {
        super(message);
    }

    public CMessagingException(String message, Throwable cause) {
        super(message, cause);
    }
}
