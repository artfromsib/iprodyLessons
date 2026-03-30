package com.ym.paymentservice.interfaces.exception;

public class BaseServiceException extends RuntimeException {
    public BaseServiceException(String message,
                                Throwable cause) {
        super(message, cause);
    }
}
