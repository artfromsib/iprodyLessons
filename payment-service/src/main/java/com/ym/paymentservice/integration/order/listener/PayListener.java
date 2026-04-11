package com.ym.paymentservice.integration.order.listener;

import com.ym.paymentservice.application.service.PaymentService;
import com.ym.paymentservice.domain.model.enums.PaymentStatus;
import com.ym.paymentservice.integration.order.config.properties.RabbitMqOrderServiceProperties;
import com.ym.paymentservice.integration.order.dto.request.PayRequestDTO;
import com.ym.paymentservice.integration.order.dto.response.PayResponseDTO;
import com.ym.paymentservice.interfaces.dto.PaymentResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PayListener {
    private final PaymentService paymentService;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqOrderServiceProperties properties;


    @RabbitListener(queues = "${rabbitmq.service.order.queue-request-name:payment-request-queue}")
    public void handle(PayRequestDTO request) {
        log.info("Received pay request: orderId={}", request.orderId());
        PaymentResponseDTO responseEntity = paymentService.payProcess(request);
        boolean paid = responseEntity.getStatus().equals(PaymentStatus.SUCCESS.name());

        log.info("Pay result for orderId={}: paid={}", request.orderId(), paid);

        PayResponseDTO response = new PayResponseDTO(paid, request.orderId().toString());
        rabbitTemplate.convertAndSend(
                properties.exchangeResponseName(),
                properties.queueResponseName(),
                response
        );
        log.info("Sent pay response for orderId={}", request.orderId());
    }
}
