package com.ym.orderservice.saga;

import com.ym.orderservice.infrastructure.web.dto.message.OrderCreationStatus;
import com.ym.orderservice.infrastructure.web.dto.message.OrderCreationStatusMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreationSagaOrchestrator {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(
            topics = "${kafka.service.order.order-creation-status-topic}",
            groupId = "saga-group"
    )
    public void handleSagaEvents(OrderCreationStatusMessage message, Acknowledgment acknowledgment) {
        UUID orderId = message.orderId();
        OrderCreationStatus status = message.status();

        log.info("Received saga event for orderId: {}, status: {}", orderId, status);

        switch (status) {
            case ORDER_CREATE -> {
                log.info("Handling ORDER_CREATE status, sending create payment for orderId: {}", orderId);
                sendOrderPaymentMessage(message, orderId);
            }

            case PAYMENT_SUCCESSFUL -> {
                log.info("Payment successful, sending delivery create message for orderId: {}", orderId);
                sendDeliveryCreateMessage(orderId);
            }

            case PAYMENT_FAILED -> {
                log.info("Payment failed, sending cancel order for orderId: {}", orderId);
                sendCancelOrderMessage(orderId);
            }

            case DELIVERY_CREATED -> {
                log.info("Delivery created, sending message for orderId: {}", orderId);
                sendDeliveryCreatedOrderMessage(orderId);
            }

            case DELIVERY_FAILED -> {
                log.warn("Delivery failed for orderId: {}, cancelling  payment and cancelling order", orderId);
                sendPaymentCancelMessage(orderId);
                sendCancelOrderMessage(orderId);
            }

            default -> log.warn("Unknown status: {} for orderId: {}", status, orderId);
        }

        acknowledgment.acknowledge();
    }

    private void sendOrderPaymentMessage(OrderCreationStatusMessage message,
                                         UUID orderId) {
        Object order = OrderCreationStatusMessage.builder()
                .orderId(orderId)
                .customerId(message.customerId())
                .amount(message.amount())
                .currency(message.currency())
                .build();

        kafkaTemplate.send("payment.order.create.request", order);
    }

    private void sendDeliveryCreateMessage(UUID orderId) {
        var statusMessage = OrderCreationStatusMessage.builder()
                .orderId(orderId)
                .build();
        kafkaTemplate.send("delivery.create.request", statusMessage);
    }

    private void sendCancelOrderMessage(UUID orderId) {
        var statusMessage = OrderCreationStatusMessage.builder()
                .orderId(orderId)
                .status(OrderCreationStatus.CANCEL)
                .build();
        kafkaTemplate.send("order.creation.status", statusMessage);
    }

    private void sendDeliveryCreatedOrderMessage(UUID orderId) {
        var statusMessage =OrderCreationStatusMessage.builder()
                .orderId(orderId)
                .status(OrderCreationStatus.SHIPPED)
                .build();
        kafkaTemplate.send("order.creation.status", statusMessage);
    }

    private void sendPaymentCancelMessage(UUID orderId) {
        OrderCreationStatusMessage order = OrderCreationStatusMessage.builder()
                .orderId(orderId)
                .build();
        kafkaTemplate.send("payment.cancellation.release.request", order);
    }
}
