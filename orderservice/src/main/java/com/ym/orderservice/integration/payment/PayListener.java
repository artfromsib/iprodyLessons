package com.ym.orderservice.integration.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ym.orderservice.application.service.OrderService;

import com.ym.orderservice.integration.payment.dto.response.PayResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PayListener {
    private final OrderService orderService;

    @RabbitListener(queues = "${rabbitmq.service.payment.queue-response-name}")
    public void handle(PayResponseDTO response) throws JsonProcessingException {
        log.info("Received payment response: orderId={}, paid={}", response.orderId(), response.paid());
        orderService.changeOrderStatus(response.orderId(), response.paid());
        log.info("Successfully updated order status for orderId={}", response.orderId());
    }
}
