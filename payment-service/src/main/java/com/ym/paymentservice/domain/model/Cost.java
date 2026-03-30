package com.ym.paymentservice.domain.model;

import lombok.Value;

import java.math.BigDecimal;
import java.util.Currency;

@Value
public class Cost {
    BigDecimal amount;
    Currency currency;

    public Cost(BigDecimal amount, Currency currency) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        } else {
            this.amount = amount;

        }
        if (currency == null) {
            throw new IllegalArgumentException("Currency cannot be null");
        } else
            this.currency = currency;
    }
}