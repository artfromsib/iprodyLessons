package com.ym.orderservice.integration.delivery.dto.request;

import lombok.*;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPaidRequestMessage {
    private UUID orderId;
}