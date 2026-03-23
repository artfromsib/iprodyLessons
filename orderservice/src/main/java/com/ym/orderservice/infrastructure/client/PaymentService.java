package com.ym.orderservice.infrastructure.client;

import com.ym.orderservice.infrastructure.web.dto.OrderResponse;

import java.util.Optional;

public interface PaymentService {
  Optional<OrderResponse> payOrder(OrderResponse response);
}
