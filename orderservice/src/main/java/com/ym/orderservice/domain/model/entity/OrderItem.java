package com.ym.orderservice.domain.model.entity;

import lombok.Getter;
import java.math.BigDecimal;

@Getter
public class OrderItem {
  private final Long productId;
  private final String productName;
  private final BigDecimal price;
  private final int quantity;
  private final BigDecimal subtotal;

  public OrderItem(Long productId, String productName, BigDecimal price, int quantity) {
    if (productId == null || productId <= 0) {
      throw new IllegalArgumentException("Product ID must be positive");
    }
    if (productName == null || productName.trim().isEmpty()) {
      throw new IllegalArgumentException("Product name cannot be null or empty");
    }
    if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Price cannot be null or negative");
    }
    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantity must be positive");
    }

    this.productId = productId;
    this.productName = productName;
    this.price = price;
    this.quantity = quantity;
    this.subtotal = price.multiply(BigDecimal.valueOf(quantity));
  }
}