package com.ym.deliveryservice.integration.order.dto.request;

import java.util.UUID;

public record DeliveryCreateRequestMessage (
        UUID orderId
){
}
