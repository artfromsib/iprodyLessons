package src.main.java.com.ym.integration.order.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Builder
public record OrderCreationStatusMessage (
        UUID orderId,
        OrderCreationStatus status,
        String trackingNumber,
        Long customerId,
        BigDecimal amount,
        Currency currency
){
}
