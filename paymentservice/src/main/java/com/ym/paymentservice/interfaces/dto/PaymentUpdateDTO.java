package com.ym.paymentservice.interfaces.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentUpdateDTO {
  private String status;
  private String providerTransactionId;
  private String providerName;
  private String rawResponse;
  private String failureReason;
}