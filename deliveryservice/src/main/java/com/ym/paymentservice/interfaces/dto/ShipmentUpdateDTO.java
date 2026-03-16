package com.ym.paymentservice.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentUpdateDTO {
  private String status;
  private String trackingNumber;
  private LocalDateTime estimatedDeliveryDate;
}