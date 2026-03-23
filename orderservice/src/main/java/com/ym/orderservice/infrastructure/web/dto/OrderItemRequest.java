package com.ym.orderservice.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Товар в заказе")
public class OrderItemRequest {

  @Schema(description = "ID товара",
          example = "98765",
          required = true)
  private Long productId;

  @Schema(description = "Название товара",
          example = "Смартфон iPhone 15 Pro",
          required = true,
          maxLength = 200)
  private String productName;

  @Schema(description = "Цена за единицу",
          example = "999.99",
          required = true,
          minimum = "0.01")
  private BigDecimal price;

  @Schema(description = "Количество",
          example = "2",
          required = true,
          minimum = "1",
          maximum = "999")
  private Integer quantity;
}