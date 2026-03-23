package com.ym.orderservice.integration.payment.dto.request;

import java.math.BigDecimal;
import java.util.Currency;

public record PayRequestDTO(Long customerId, java.util.UUID orderId, BigDecimal amount, Currency currency) {
}
