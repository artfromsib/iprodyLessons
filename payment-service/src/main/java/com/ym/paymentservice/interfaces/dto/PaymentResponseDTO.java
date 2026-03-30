package com.ym.paymentservice.interfaces.dto;

import com.ym.paymentservice.domain.model.Payment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ с информацией о платеже")
public class PaymentResponseDTO {

    @Schema(description = "Идентификатор платежа", example = "pay_123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Идентификатор заказа", example = "123e4567-e89b-12d3-a456-426614174000")
    private String orderId;

    @Schema(description = "Идентификатор клиента", example = "12345")
    private Long customerId;

    @Schema(description = "Сумма платежа", example = "1000.50")
    private BigDecimal amount;

    @Schema(description = "Валюта платежа", example = "RUB")
    private String currency;

    @Schema(description = "Статус платежа", example = "COMPLETED", allowableValues = {"PENDING", "COMPLETED", "FAILED", "CANCELLED"})
    private String status;

    @Schema(description = "Тип платежного метода", example = "CREDIT_CARD")
    private String paymentMethodType;

    @Schema(description = "Токен провайдера", example = "tok_visa_4242")
    private String providerToken;

    @Schema(description = "ID транзакции у провайдера", example = "txn_123456789")
    private String providerTransactionId;

    @Schema(description = "Название провайдера платежей", example = "STRIPE")
    private String providerName;

    @Schema(description = "Дата создания платежа", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Дата последнего обновления", example = "2024-01-15T10:35:00")
    private LocalDateTime updatedAt;

    /**
     * Преобразует доменную сущность Payment в DTO для ответа
     */
    public static PaymentResponseDTO fromDomain(Payment payment) {
        return PaymentResponseDTO.builder()
                .id(payment.getId() != null ? payment.getId().getValue() : null)
                .orderId(payment.getOrderId() != null ? payment.getOrderId().getValue() : null)
                .customerId(payment.getCustomerId() != null ? payment.getCustomerId().getValue() : null)
                .amount(payment.getCost() != null ? payment.getCost().getAmount() : null)
                .currency(payment.getCost() != null ? payment.getCost().getCurrency().getCurrencyCode() : null)
                .status(payment.getStatus() != null ? payment.getStatus().name() : null)
                .paymentMethodType(payment.getPaymentMethod() != null ?
                        payment.getPaymentMethod().getType().name() : null)
                .providerToken(payment.getPaymentMethod() != null ?
                        payment.getPaymentMethod().getProviderToken() : null)
                .providerTransactionId(payment.getTransactionDetails() != null ?
                        payment.getTransactionDetails().getProviderTransactionId() : null)
                .providerName(payment.getTransactionDetails() != null ?
                        payment.getTransactionDetails().getProviderName() : null)
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}