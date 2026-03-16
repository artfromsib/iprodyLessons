package com.ym.paymentservice.domain.model;

import lombok.Value;

@Value
public class CustomerId {
  String value;

  public CustomerId(String value) {
    if (value == null || value.trim().isEmpty()) {
      throw new IllegalArgumentException("Order ID cannot be empty");
    }
    this.value = value;
  }
}