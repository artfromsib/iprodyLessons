package com.ym.orderservice.infrastructure.client.impl;

import com.ym.orderservice.domain.repository.OrderRepository;
import com.ym.orderservice.infrastructure.client.PaymentService;
import com.ym.orderservice.infrastructure.web.dto.OrderResponse;
import com.ym.orderservice.integration.payment.client.PaymentClient;
import com.ym.orderservice.integration.payment.dto.request.PayRequestDTO;
import com.ym.orderservice.integration.payment.dto.response.PayResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Currency;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
  private final OrderRepository repository;
  private final PaymentClient paymentClient;
  @Override
 public Optional<OrderResponse> payOrder(OrderResponse order){
    PayResponseDTO response = paymentClient.payOrder( new PayRequestDTO(order.getCustomer().getId(),
            order.getId(), order.getTotalAmount(), Currency.getInstance("USD") ));
    if (response.paid()) {
      return Optional.of(order);
    } else {
      return Optional.empty();
    }
  }


}
