package com.ym.paymentservice.interfaces.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Ответ на запрос обработки платежа")
public record PayResponseDTO(
        @Schema(description = "Статус оплаты", example = "true")
        boolean paid,

        @Schema(description = "Идентификатор заказа", example = "123e4567-e89b-12d3-a456-426614174000")
        String orderId
) {
}
