package com.ym.deliveryservice.integration.order.dto.response;

import lombok.Builder;

@Builder
public record  DeliveryCreatedResponseMessage (
        String orderId,
        String shipmentId
) {
}
