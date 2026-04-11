package com.ym.paymentservice.integration.order.dto.request;

import java.math.BigDecimal;
import java.util.Currency;

public record PayRequestDTO(Long customerId, java.util.UUID orderId, BigDecimal amount, Currency currency) {
}
