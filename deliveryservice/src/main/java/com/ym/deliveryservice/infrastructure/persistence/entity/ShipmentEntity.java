package com.ym.deliveryservice.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "shipments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentEntity {

  @Id
  private String id;

  @Column(name = "order_id", nullable = false, unique = true)
  private String orderId;

  @Column(name = "tracking_number", nullable = false, unique = true)
  private String trackingNumber;

  @Column(name = "street", nullable = false)
  private String street;

  @Column(name = "city", nullable = false)
  private String city;

  @Column(name = "state")
  private String state;

  @Column(name = "zip_code", nullable = false)
  private String zipCode;

  @Column(name = "country", nullable = false)
  private String country;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private DeliveryStatus status;

  @Enumerated(EnumType.STRING)
  @Column(name = "delivery_option", nullable = false)
  private DeliveryOption deliveryOption;

  @Column(name = "shipping_cost_amount", nullable = false, precision = 19, scale = 2)
  private BigDecimal shippingCostAmount;

  @Column(name = "shipping_cost_currency", nullable = false)
  private String shippingCostCurrency;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "estimated_delivery_date")
  private LocalDateTime estimatedDeliveryDate;

  @Column(name = "actual_delivery_date")
  private LocalDateTime actualDeliveryDate;

  public enum DeliveryStatus {
    PENDING, WITH_COURIER, IN_TRANSIT, DELIVERED
  }

  public enum DeliveryOption {
    COURIER, PICKUP_POINT, POST
  }
}
