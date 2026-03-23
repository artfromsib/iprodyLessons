package com.ym.deliveryservice.interfaces.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на обновление отправления")
public class ShipmentUpdateDTO {

  @Schema(description = "Статус отправления", example = "DELIVERED",
          allowableValues = {"PENDING", "PROCESSING", "SHIPPED", "IN_TRANSIT", "DELIVERED", "CANCELLED"})
  private String status;

  @Schema(description = "Трек-номер отправления", example = "TRK98765432")
  private String trackingNumber;

  @Schema(description = "Предполагаемая дата доставки", example = "2024-02-05T15:00:00")
  private LocalDateTime estimatedDeliveryDate;
}