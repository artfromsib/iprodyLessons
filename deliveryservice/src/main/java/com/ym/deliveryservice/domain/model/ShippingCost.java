package com.ym.deliveryservice.domain.model;

import lombok.Value;
import java.math.BigDecimal;
import java.util.Currency;

@Value
public class ShippingCost {
  BigDecimal amount;
  Currency currency;

  public ShippingCost(BigDecimal amount, Currency currency) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Amount must be non-negative");
    }
    if (currency == null) {
      throw new IllegalArgumentException("Currency cannot be null");
    }
    this.amount = amount;
    this.currency = currency;
  }

  public ShippingCost add(ShippingCost other) {
    if (!this.currency.equals(other.currency)) {
      throw new IllegalArgumentException("Cannot add different currencies");
    }
    return new ShippingCost(this.amount.add(other.amount), this.currency);
  }
}