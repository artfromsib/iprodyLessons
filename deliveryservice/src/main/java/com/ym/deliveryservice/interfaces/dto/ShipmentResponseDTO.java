package com.ym.deliveryservice.interfaces.dto;

import com.ym.deliveryservice.domain.model.Shipment;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Ответ с информацией об отправлении")
public class ShipmentResponseDTO {

  @Schema(description = "Идентификатор отправления", example = "ship_123e4567-e89b-12d3-a456-426614174000")
  private String id;

  @Schema(description = "Идентификатор заказа", example = "123e4567-e89b-12d3-a456-426614174000")
  private String orderId;

  @Schema(description = "Трек-номер отправления", example = "TRK12345678")
  private String trackingNumber;

  @Schema(description = "Улица", example = "ул. Тверская, д. 15")
  private String street;

  @Schema(description = "Город", example = "Москва")
  private String city;

  @Schema(description = "Область/Регион", example = "Московская область")
  private String state;

  @Schema(description = "Почтовый индекс", example = "125009")
  private String zipCode;

  @Schema(description = "Страна", example = "Россия")
  private String country;

  @Schema(description = "Полный адрес", example = "Россия, Москва, ул. Тверская, д. 15, 125009")
  private String fullAddress;

  @Schema(description = "Статус отправления", example = "IN_TRANSIT",
          allowableValues = {"PENDING", "PROCESSING", "SHIPPED", "IN_TRANSIT", "DELIVERED", "CANCELLED"})
  private String status;

  @Schema(description = "Вариант доставки", example = "EXPRESS",
          allowableValues = {"STANDARD", "EXPRESS", "SAME_DAY", "PICKUP"})
  private String deliveryOption;

  @Schema(description = "Стоимость доставки", example = "500.00")
  private BigDecimal shippingCost;

  @Schema(description = "Валюта", example = "RUB")
  private String currency;

  @Schema(description = "Дата создания", example = "2024-01-15T10:30:00")
  private LocalDateTime createdAt;

  @Schema(description = "Дата последнего обновления", example = "2024-01-15T10:35:00")
  private LocalDateTime updatedAt;

  @Schema(description = "Предполагаемая дата доставки", example = "2024-02-01T15:00:00")
  private LocalDateTime estimatedDeliveryDate;

  @Schema(description = "Фактическая дата доставки", example = "2024-01-30T14:30:00")
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