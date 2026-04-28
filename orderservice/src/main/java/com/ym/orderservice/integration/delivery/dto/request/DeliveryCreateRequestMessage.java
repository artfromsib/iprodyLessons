package com.ym.orderservice.integration.delivery.dto.request;

import java.util.UUID;

public record DeliveryCreateRequestMessage (
        UUID orderId
) {
}
