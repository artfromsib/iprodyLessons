package com.ym.orderservice.integration.payment.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record PayResponseDTO(
        boolean paid,
        UUID orderId) {
}
