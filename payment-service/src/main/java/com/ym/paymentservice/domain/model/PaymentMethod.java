package com.ym.paymentservice.domain.model;

import lombok.Value;

@Value
public class PaymentMethod {
  PaymentMethodType type;
  String providerToken;

  public enum PaymentMethodType {
    CREDIT_CARD,
    SBP
  }

  public PaymentMethod( PaymentMethodType type, String providerToken) {
    if (type == null) {
      throw new IllegalArgumentException("Payment method type cannot be null");
    } else
      this.type = type;
    if (providerToken == null || providerToken.trim().isEmpty()) {
      throw new IllegalArgumentException("Provider token cannot be empty");
    } else
      this.providerToken = providerToken;
  }
}