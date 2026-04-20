package com.ym.orderservice.integration.delivery.dto.response;

import java.util.UUID;

public record DeliveryCreatedResponseMessage(
        UUID orderId,
        UUID deliveryConfirmationId
) {
}
