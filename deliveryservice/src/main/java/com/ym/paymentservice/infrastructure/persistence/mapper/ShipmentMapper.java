package com.ym.paymentservice.infrastructure.persistence.mapper;


import com.ym.paymentservice.domain.model.*;
import com.ym.paymentservice.infrastructure.persistence.entity.ShipmentEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Currency;

@Component
public class ShipmentMapper {

  public ShipmentEntity toEntity(Shipment shipment) {
    if (shipment == null) return null;

    ShipmentEntity.ShipmentEntityBuilder builder = ShipmentEntity.builder()
            .id(shipment.getId() != null ? shipment.getId().getValue() : null)
            .orderId(shipment.getOrderId() != null ? shipment.getOrderId().getValue() : null)
            .trackingNumber(shipment.getTrackingNumber() != null ?
                    shipment.getTrackingNumber().getValue() : null)
            .status(mapStatusToEntity(shipment.getStatus()))
            .deliveryOption(mapOptionToEntity(shipment.getDeliveryOption()))
            .createdAt(shipment.getCreatedAt())
            .updatedAt(shipment.getUpdatedAt())
            .estimatedDeliveryDate(shipment.getEstimatedDeliveryDate())
            .actualDeliveryDate(shipment.getActualDeliveryDate());

    if (shipment.getShippingAddress() != null) {
      builder.street(shipment.getShippingAddress().getStreet())
              .city(shipment.getShippingAddress().getCity())
              .state(shipment.getShippingAddress().getState())
              .zipCode(shipment.getShippingAddress().getZipCode())
              .country(shipment.getShippingAddress().getCountry());
    }

    if (shipment.getShippingCost() != null) {
      builder.shippingCostAmount(shipment.getShippingCost().getAmount())
              .shippingCostCurrency(shipment.getShippingCost().getCurrency().getCurrencyCode());
    }

    return builder.build();
  }

  public Shipment toDomain(ShipmentEntity entity) {
    if (entity == null) return null;

    ShipmentId shipmentId = entity.getId() != null ? new ShipmentId(entity.getId()) : null;
    OrderId orderId = entity.getOrderId() != null ? new OrderId(entity.getOrderId()) : null;
    TrackingNumber trackingNumber = entity.getTrackingNumber() != null ?
            new TrackingNumber(entity.getTrackingNumber()) : null;

    ShippingAddress address = new ShippingAddress(
            entity.getStreet(),
            entity.getCity(),
            entity.getState(),
            entity.getZipCode(),
            entity.getCountry()
    );

    DeliveryStatus status = mapStatusToDomain(entity.getStatus());
    DeliveryOption option = mapOptionToDomain(entity.getDeliveryOption());

    ShippingCost cost = null;
    if (entity.getShippingCostAmount() != null && entity.getShippingCostCurrency() != null) {
      cost = new ShippingCost(
              entity.getShippingCostAmount(),
              Currency.getInstance(entity.getShippingCostCurrency())
      );
    }

    return Shipment.builder()
            .id(shipmentId)
            .orderId(orderId)
            .trackingNumber(trackingNumber)
            .shippingAddress(address)
            .status(status)
            .deliveryOption(option)
            .shippingCost(cost)
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .estimatedDeliveryDate(entity.getEstimatedDeliveryDate())
            .actualDeliveryDate(entity.getActualDeliveryDate())
            .build();
  }

  private ShipmentEntity.DeliveryStatus mapStatusToEntity(DeliveryStatus status) {
    if (status == null) return null;
    return ShipmentEntity.DeliveryStatus.valueOf(status.name());
  }

  private DeliveryStatus mapStatusToDomain(ShipmentEntity.DeliveryStatus status) {
    if (status == null) return null;
    return DeliveryStatus.valueOf(status.name());
  }

  private ShipmentEntity.DeliveryOption mapOptionToEntity(DeliveryOption option) {
    if (option == null) return null;
    return ShipmentEntity.DeliveryOption.valueOf(option.name());
  }

  private DeliveryOption mapOptionToDomain(ShipmentEntity.DeliveryOption option) {
    if (option == null) return null;
    return DeliveryOption.valueOf(option.name());
  }
}