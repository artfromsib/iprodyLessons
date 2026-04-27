package com.ym.paymentservice.application.service;

import com.ym.paymentservice.domain.model.*;
import com.ym.paymentservice.domain.model.enums.PaymentStatus;
import com.ym.paymentservice.domain.repository.PaymentRepository;
import com.ym.paymentservice.integration.order.dto.message.OrderCreationStatus;
import com.ym.paymentservice.integration.order.dto.message.OrderCreationStatusMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${kafka.service.order.order-creation-status-topic}")
    private String orderCreationTopic;


    @Transactional
    public void createPayment(OrderCreationStatusMessage message) {
        boolean paid;
        PaymentId paymentId = new PaymentId(UUID.randomUUID().toString());
        OrderId orderId = new OrderId(message.orderId().toString());
        CustomerId customerId = new CustomerId(message.customerId());

        Cost cost = new Cost(message.amount(),message.currency());

        Payment payment = Payment.builder()
                .id(paymentId)
                .orderId(orderId)
                .customerId(customerId)
                .cost(cost)
                .status(PaymentStatus.SUCCESS)
                .createdAt(LocalDateTime.now())
                .build();

        Payment savedPayment = paymentRepository.save(payment);
        if (savedPayment != null) {
            paid = true;
        } else {
            paid = false;
        }
        log.info("Payment created with ID: {}", savedPayment.getId().getValue());
        sendStatusMessage(UUID.fromString(orderId.getValue()), paid);
    }


    private void sendStatusMessage(UUID orderId,
                                   boolean paid) {
        var statusMessage = OrderCreationStatusMessage.builder()
                .orderId(orderId)
                .status(paid ? OrderCreationStatus.PAYMENT_SUCCESSFUL : OrderCreationStatus.PAYMENT_FAILED)
                .build();

        kafkaTemplate.send(orderCreationTopic, statusMessage);
    }

    public void deletePaymentByOrderId(UUID orderId) {
        paymentRepository.deleteByOrderId(new OrderId(orderId.toString()));
    }

}