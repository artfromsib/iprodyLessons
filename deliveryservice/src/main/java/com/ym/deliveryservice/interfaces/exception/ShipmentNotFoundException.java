package com.ym.deliveryservice.interfaces.exception;

public class ShipmentNotFoundException extends RuntimeException {
  public ShipmentNotFoundException(String message) {
    super(message);
  }
}