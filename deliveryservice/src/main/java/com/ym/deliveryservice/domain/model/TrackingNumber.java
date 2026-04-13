package com.ym.deliveryservice.domain.model;

import lombok.Value;

@Value
public class TrackingNumber {
    String value;

    public TrackingNumber(String value) {
        if (value != null) {
            if (!value.matches("^[A-Z0-9]{8,20}$")) {
                throw new IllegalArgumentException("Tracking number must be 8-20 alphanumeric characters");
            }

        }
        this.value = value;
    }
}