package com.ym.deliveryservice.domain.model;

import lombok.Value;

@Value
public class ShipmentId {
  String value;

  public ShipmentId(String value) {
    if (value == null || value.trim().isEmpty()) {
      throw new IllegalArgumentException("Shipment ID cannot be empty");
    }
    this.value = value;
  }
}