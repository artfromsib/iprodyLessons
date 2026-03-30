package com.ym.paymentservice.interfaces.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на создание/обработку платежа")
public class PaymentRequestDTO {

    @NotNull(message = "ID заказа не может быть null")
    @Schema(description = "Идентификатор заказа", example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    private String orderId;

    @NotNull(message = "ID клиента не может быть null")
    @Schema(description = "Идентификатор клиента", example = "12345", required = true)
    private Long customerId;

    @NotNull(message = "Сумма платежа не может быть null")
    @Schema(description = "Сумма платежа", example = "1000.50", required = true)
    private BigDecimal amount;

    @NotNull(message = "Валюта не может быть null")
    @Schema(description = "Валюта платежа", example = "RUB", required = true)
    private Currency currency;

    @Schema(description = "Тип платежного метода", example = "CREDIT_CARD", allowableValues = {"CREDIT_CARD", "DEBIT_CARD", "PAYPAL", "BANK_TRANSFER"})
    private String paymentMethodType;

    @Schema(description = "Токен провайдера платежей", example = "tok_visa_4242")
    private String providerToken;
}