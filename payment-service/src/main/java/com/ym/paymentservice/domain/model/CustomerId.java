package com.ym.paymentservice.domain.model;

import lombok.Value;

@Value
public class CustomerId {
    Long value;

    public CustomerId(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }
        if (value <= 0) {
            throw new IllegalArgumentException("Customer ID must be positive");
        }
        this.value = value;
    }
}