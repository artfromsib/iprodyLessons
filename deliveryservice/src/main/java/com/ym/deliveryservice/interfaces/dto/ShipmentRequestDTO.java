package com.ym.deliveryservice.interfaces.dto;

import com.ym.deliveryservice.domain.model.DeliveryOption;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на создание отправления")
public class ShipmentRequestDTO {

    @NotBlank(message = "Order ID is required")
    @Schema(description = "Идентификатор заказа", example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    private String orderId;

    @Pattern(regexp = "^[A-Z0-9]{8,20}$", message = "Tracking number must be 8-20 alphanumeric characters")
    @Schema(description = "Трек-номер отправления", example = "TRK12345678", pattern = "^[A-Z0-9]{8,20}$")
    private String trackingNumber;

    @Schema(description = "Улица", example = "ул. Тверская, д. 15")
    private String street;

    @Schema(description = "Город", example = "Москва")
    private String city;

    @Schema(description = "Область/Регион", example = "Московская область")
    private String state;

    @Schema(description = "Почтовый индекс", example = "125009")
    private String zipCode;

    @Schema(description = "Страна", example = "Россия")
    private String country;

    @Schema(description = "Вариант доставки", example = "STANDARD",
            allowableValues = {"STANDARD", "EXPRESS", "SAME_DAY", "PICKUP"})
    private DeliveryOption deliveryOption;

    @PositiveOrZero(message = "Shipping cost must be positive or zero")
    @Schema(description = "Стоимость доставки", example = "500.00")
    private BigDecimal shippingCost;

    @Schema(description = "Валюта", example = "RUB", required = true)
    private Currency currency;

    @Schema(description = "Предполагаемая дата доставки", example = "2024-02-01T15:00:00")
    private LocalDateTime estimatedDeliveryDate;

    public ShipmentRequestDTO(String orderId) {
        this.orderId = orderId;
    }
}