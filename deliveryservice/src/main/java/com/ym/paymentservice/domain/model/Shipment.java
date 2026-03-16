package com.ym.paymentservice.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Shipment {
  private ShipmentId id;
  private OrderId orderId;
  private TrackingNumber trackingNumber;
  private ShippingAddress shippingAddress;
  private DeliveryStatus status;
  private DeliveryOption deliveryOption;
  private ShippingCost shippingCost;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime estimatedDeliveryDate;
  private LocalDateTime actualDeliveryDate;

  public void assignToCourier() {
    if (this.status != DeliveryStatus.PENDING) {
      throw new IllegalStateException("Shipment can only be assigned to courier when in PENDING state");
    }
    this.status = DeliveryStatus.WITH_COURIER;
    this.updatedAt = LocalDateTime.now();
  }

  public void markInTransit() {
    if (this.status != DeliveryStatus.WITH_COURIER) {
      throw new IllegalStateException("Shipment can only be in transit when assigned to courier");
    }
    this.status = DeliveryStatus.IN_TRANSIT;
    this.updatedAt = LocalDateTime.now();
  }

  public void markAsDelivered() {
    if (this.status != DeliveryStatus.IN_TRANSIT) {
      throw new IllegalStateException("Shipment can only be delivered when in transit");
    }
    this.status = DeliveryStatus.DELIVERED;
    this.actualDeliveryDate = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  public void updateTrackingNumber(String newTrackingNumber) {
    if (this.status == DeliveryStatus.DELIVERED) {
      throw new IllegalStateException("Cannot update tracking number for delivered shipment");
    }
    this.trackingNumber = new TrackingNumber(newTrackingNumber);
    this.updatedAt = LocalDateTime.now();
  }
}
