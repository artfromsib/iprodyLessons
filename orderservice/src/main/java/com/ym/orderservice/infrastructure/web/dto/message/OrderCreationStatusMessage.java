package com.ym.orderservice.infrastructure.web.dto.message;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
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
