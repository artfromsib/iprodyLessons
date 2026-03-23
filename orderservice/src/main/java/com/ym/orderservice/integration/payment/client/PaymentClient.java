package com.ym.orderservice.integration.payment.client;

import com.ym.orderservice.integration.payment.client.feign.PaymentFeignClient;
import com.ym.orderservice.integration.payment.dto.request.PayRequestDTO;
import com.ym.orderservice.integration.payment.dto.response.PayResponseDTO;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.nio.ByteBuffer;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class PaymentClient {
  private final PaymentFeignClient paymentFeignClient;
  private final JsonMapper mapper;

  public PayResponseDTO payOrder(PayRequestDTO req) {
    try {
      return paymentFeignClient.payOrder(req, req.orderId());
    } catch (FeignException ex) {
      return processException(ex);
    }
  }

  private PayResponseDTO processException(FeignException ex) {
    if (!isErrorAcceptable(ex)) {
      throw new RuntimeException("Не удалось оплатить заказ");
    }

    return extractResponseFromException(ex);
  }

  private boolean isErrorAcceptable(FeignException ex) {
    HttpStatusCode statusCode = HttpStatusCode.valueOf(ex.status());
    return (statusCode.is2xxSuccessful() || statusCode.isSameCodeAs(HttpStatus.CONFLICT))
            && ex.responseBody().isPresent();
  }

  private PayResponseDTO extractResponseFromException(FeignException ex) {
    ByteBuffer body = ex.responseBody()
            .orElseThrow(() -> new RuntimeException("Тело ответа отсутствует"));

    return mapper.readValue(body.array(), PayResponseDTO.class);
  }
}
