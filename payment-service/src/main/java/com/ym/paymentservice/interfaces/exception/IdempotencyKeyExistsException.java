package com.ym.paymentservice.interfaces.exception;

public class IdempotencyKeyExistsException extends BaseServiceException {

  public IdempotencyKeyExistsException(String message,
                                       Throwable cause) {
    super(message, cause);
  }
}
