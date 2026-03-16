package com.ym.paymentservice.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Entity
@Table(name = "payments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {

  @Id
  private String id;

  @Column(name = "order_id", nullable = false)
  private String orderId;

  @Column(name = "customer_id", nullable = false)
  private String customerId;

  @Column(name = "amount", nullable = false, precision = 19, scale = 2)
  private BigDecimal amount;

  @Column(name = "currency", nullable = false)
  private String currency;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private PaymentStatus status;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_method_type", nullable = false)
  private PaymentMethodType paymentMethodType;

  @Column(name = "provider_token", nullable = false)
  private String providerToken;

  @Column(name = "provider_transaction_id")
  private String providerTransactionId;

  @Column(name = "provider_name")
  private String providerName;

  @Column(name = "raw_response", length = 2000)
  private String rawResponse;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public enum PaymentStatus {
    PENDING, PROCESSING, SUCCESS, FAILED, REFUNDED
  }

  public enum PaymentMethodType {
    CREDIT_CARD, SBP
  }
}