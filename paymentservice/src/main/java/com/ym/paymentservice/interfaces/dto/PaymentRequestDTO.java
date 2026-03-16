package com.ym.paymentservice.interfaces.dto;

import com.ym.paymentservice.domain.model.Cost;
import com.ym.paymentservice.domain.model.Payment;
import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {

  @NonNull
  private String orderId;

  @NonNull
  private String customerId;

  @NonNull
  private BigDecimal amount;

  @NonNull
  private Currency currency;

  @NonNull
  private String paymentMethodType;

  @NonNull
  private String providerToken;
}