package com.ym.orderservice.infrastructure.web.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemRequest {
  private Long productId;
  private String productName;
  private BigDecimal price;
  private Integer quantity;
}