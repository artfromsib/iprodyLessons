package com.ym.deliveryservice.integration.order.dto.message;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Builder
public record OrderCreationStatusMessage (
        UUID orderId,
        OrderCreationStatus status,
        Long customerId,
        BigDecimal amount,
        Currency currency
){
}
