package com.ym.orderservice.application.exception;

public class SendingAsyncMessageException extends RuntimeException {

    public SendingAsyncMessageException(String message,
                                        Throwable cause) {
        super(message, cause);
    }
}
