package com.ym.paymentservice.interfaces.dto;

import com.ym.paymentservice.domain.model.Payment;
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
public class PaymentResponseDTO {
  private String id;
  private String orderId;
  private String customerId;
  private BigDecimal amount;
  private String currency;
  private String status;
  private String paymentMethodType;
  private String providerToken;
  private String providerTransactionId;
  private String providerName;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static PaymentResponseDTO fromDomain(Payment payment) {
    return PaymentResponseDTO.builder()
            .id(payment.getId() != null ? payment.getId().getValue() : null)
            .orderId(payment.getOrderId() != null ? payment.getOrderId().getValue() : null)
            .customerId(payment.getCustomerId() != null ? payment.getCustomerId().getValue() : null)
            .amount(payment.getCost() != null ? payment.getCost().getAmount() : null)
            .currency(payment.getCost() != null ? payment.getCost().getCurrency().getCurrencyCode() : null)
            .status(payment.getStatus() != null ? payment.getStatus().name() : null)
            .paymentMethodType(payment.getPaymentMethod() != null ?
                    payment.getPaymentMethod().getType().name() : null)
            .providerToken(payment.getPaymentMethod() != null ?
                    payment.getPaymentMethod().getProviderToken() : null)
            .providerTransactionId(payment.getTransactionDetails() != null ?
                    payment.getTransactionDetails().getProviderTransactionId() : null)
            .providerName(payment.getTransactionDetails() != null ?
                    payment.getTransactionDetails().getProviderName() : null)
            .createdAt(payment.getCreatedAt())
            .updatedAt(payment.getUpdatedAt())
            .build();
  }
}