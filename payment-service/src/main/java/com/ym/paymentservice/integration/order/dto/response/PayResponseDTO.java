package com.ym.paymentservice.integration.order.dto.response;

public record PayResponseDTO(
        boolean paid,
        String orderId) {
}