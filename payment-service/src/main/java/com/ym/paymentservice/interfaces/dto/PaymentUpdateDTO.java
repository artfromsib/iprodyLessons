package com.ym.paymentservice.interfaces.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на обновление платежа")
public class PaymentUpdateDTO {

    @Schema(description = "Статус платежа", example = "COMPLETED", allowableValues = {"PENDING", "COMPLETED", "FAILED", "CANCELLED"})
    private String status;

    @Schema(description = "ID транзакции у провайдера", example = "txn_123456789")
    private String providerTransactionId;

    @Schema(description = "Название провайдера", example = "STRIPE")
    private String providerName;

    @Schema(description = "Сырой ответ от провайдера", example = "{\"status\":\"success\",\"transaction_id\":\"txn_123\"}")
    private String rawResponse;

    @Schema(description = "Причина неудачи", example = "Недостаточно средств")
    private String failureReason;
}