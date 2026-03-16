package com.ym.paymentservice.domain.model;

import lombok.Value;

@Value
public class TransactionDetails {
  String providerTransactionId;
  String providerName;
  String rawResponse;

  public TransactionDetails(String providerTransactionId, String providerName, String rawResponse) {
    if (providerTransactionId == null || providerTransactionId.trim().isEmpty()) {
      throw new IllegalArgumentException("Provider transaction ID cannot be empty");
    } else
      this.providerTransactionId = providerTransactionId;
    if (providerName == null || providerName.trim().isEmpty()) {
      throw new IllegalArgumentException("Provider name cannot be empty");
    } else
      this.providerName = providerName;
    this.rawResponse = rawResponse;
  }


}