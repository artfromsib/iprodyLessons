package com.ym.orderservice.integration.payment.client.feign;

import com.ym.orderservice.integration.payment.dto.request.PayRequestDTO;
import com.ym.orderservice.integration.payment.dto.response.PayResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "payment-service", url = "http://localhost:8081/api/payments")
public interface PaymentFeignClient {
  @PostMapping("/pay")
  public PayResponseDTO payOrder(@RequestBody PayRequestDTO req, @RequestHeader ("X-Idempotency-key") UUID idempotencyKey);
}
