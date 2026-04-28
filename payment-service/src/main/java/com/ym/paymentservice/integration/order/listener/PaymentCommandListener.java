package com.ym.paymentservice.integration.order.listener;

import com.ym.paymentservice.application.service.PaymentService;
import com.ym.paymentservice.integration.order.dto.message.OrderCreationStatusMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentCommandListener {
    private final PaymentService paymentService;
    @KafkaListener(
            topics = "${kafka.service.payment.payment-order-request-topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeReservationRequest(OrderCreationStatusMessage message) {
        log.info("Received order payment request for orderId: {}, customerId: {}",
                message, message.customerId());
        paymentService.createPayment(message.orderId(),message.customerId(), message.amount(), message.currency() );
    }

    @KafkaListener(
            topics = "${kafka.service.payment.payment-cancellation-request-topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeReleaseRequest(OrderCreationStatusMessage message) {
        log.info("Received payment cancellation request for orderId: {}", message.orderId());
        paymentService.deletePaymentByOrderId(message.orderId());
    }

}
