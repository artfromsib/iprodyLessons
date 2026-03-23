package com.ym.orderservice.integration.payment.dto.response;

import lombok.Builder;

@Builder
public record PayResponseDTO(
        boolean paid,
        String orderId) {
}
