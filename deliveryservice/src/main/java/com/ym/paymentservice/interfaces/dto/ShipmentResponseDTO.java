package com.ym.paymentservice.interfaces.dto;

import com.ym.paymentservice.domain.model.Shipment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentResponseDTO {
  private String id;
  private String orderId;
  private String trackingNumber;
  private String street;
  private String city;
  private String state;
  private String zipCode;
  private String country;
  private String fullAddress;
  private String status;
  private String deliveryOption;
  private BigDecimal shippingCost;
  private String currency;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime estimatedDeliveryDate;
  private LocalDateTime actualDeliveryDate;

  public static ShipmentResponseDTO fromDomain(Shipment shipment) {
    return ShipmentResponseDTO.builder()
            .id(shipment.getId() != null ? shipment.getId().getValue() : null)
            .orderId(shipment.getOrderId() != null ? shipment.getOrderId().getValue() : null)
            .trackingNumber(shipment.getTrackingNumber() != null ?
                    shipment.getTrackingNumber().getValue() : null)
            .street(shipment.getShippingAddress() != null ?
                    shipment.getShippingAddress().getStreet() : null)
            .city(shipment.getShippingAddress() != null ?
                    shipment.getShippingAddress().getCity() : null)
            .state(shipment.getShippingAddress() != null ?
                    shipment.getShippingAddress().getState() : null)
            .zipCode(shipment.getShippingAddress() != null ?
                    shipment.getShippingAddress().getZipCode() : null)
            .country(shipment.getShippingAddress() != null ?
                    shipment.getShippingAddress().getCountry() : null)
            .fullAddress(shipment.getShippingAddress() != null ?
                    shipment.getShippingAddress().getFullAddress() : null)
            .status(shipment.getStatus() != null ? shipment.getStatus().name() : null)
            .deliveryOption(shipment.getDeliveryOption() != null ?
                    shipment.getDeliveryOption().name() : null)
            .shippingCost(shipment.getShippingCost() != null ?
                    shipment.getShippingCost().getAmount() : null)
            .currency(shipment.getShippingCost() != null ?
                    shipment.getShippingCost().getCurrency().getCurrencyCode() : null)
            .createdAt(shipment.getCreatedAt())
            .updatedAt(shipment.getUpdatedAt())
            .estimatedDeliveryDate(shipment.getEstimatedDeliveryDate())
            .actualDeliveryDate(shipment.getActualDeliveryDate())
            .build();
  }
}