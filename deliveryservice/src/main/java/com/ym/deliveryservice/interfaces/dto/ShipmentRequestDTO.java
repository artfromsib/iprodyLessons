package com.ym.deliveryservice.interfaces.dto;

import com.ym.deliveryservice.domain.model.DeliveryOption;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
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
public class ShipmentRequestDTO {

  @NotBlank(message = "Order ID is required")
  private String orderId;

  @NotBlank(message = "Tracking number is required")
  @Pattern(regexp = "^[A-Z0-9]{8,20}$", message = "Tracking number must be 8-20 alphanumeric characters")
  private String trackingNumber;

  @NotBlank(message = "Street is required")
  private String street;

  @NotBlank(message = "City is required")
  private String city;

  private String state;

  @NotBlank(message = "Zip code is required")
  private String zipCode;

  @NotBlank(message = "Country is required")
  private String country;

  @NotNull(message = "Delivery option is required")
  private DeliveryOption deliveryOption;

  @NotNull(message = "Shipping cost is required")
  @PositiveOrZero(message = "Shipping cost must be positive or zero")
  private BigDecimal shippingCost;

  @NotNull(message = "Currency is required")
  private Currency currency;

  private LocalDateTime estimatedDeliveryDate;
}
