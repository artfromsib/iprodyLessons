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
public class Payment {
  private PaymentId id;
  private OrderId orderId;
  private CustomerId customerId;
  private Cost cost;
  private PaymentStatus status;
  private PaymentMethod paymentMethod;
  private TransactionDetails transactionDetails;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public void process() {
    if (this.status != PaymentStatus.PENDING) {
      throw new IllegalStateException("Payment can only be processed when in PENDING state");
    }
    this.status = PaymentStatus.PROCESSING;
    this.updatedAt = LocalDateTime.now();
  }

  public void complete(String providerTransactionId, String providerName, String rawResponse) {
    if (this.status != PaymentStatus.PROCESSING) {
      throw new IllegalStateException("Payment can only be completed when in PROCESSING state");
    }
    this.status = PaymentStatus.SUCCESS;
    this.transactionDetails = new TransactionDetails(providerTransactionId, providerName, rawResponse);
    this.updatedAt = LocalDateTime.now();
  }

  public void fail(String reason) {
    this.status = PaymentStatus.FAILED;
    this.updatedAt = LocalDateTime.now();
  }

  public void refund() {
    if (this.status != PaymentStatus.SUCCESS) {
      throw new IllegalStateException("Only successful payments can be refunded");
    }
    this.status = PaymentStatus.REFUNDED;
    this.updatedAt = LocalDateTime.now();
  }
}