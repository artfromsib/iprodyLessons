package com.ym.paymentservice.integration.order.listener;


import com.ym.paymentservice.application.service.PaymentService;
import com.ym.paymentservice.integration.order.dto.message.OrderCreationStatus;
import com.ym.paymentservice.integration.order.dto.message.OrderCreationStatusMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Slf4j
@Component
public class OrderCreationStatusListener {

    private final PaymentService paymentService;

    @KafkaListener(
        topics = "${kafka.service.order.order-creation-status-topic}",
        groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "orderPaidKafkaListenerContainerFactory"
    )
    public void consume(OrderCreationStatusMessage message,
                        Acknowledgment ack) {

        if (message.status() == OrderCreationStatus.ORDER_CREATE) {
            paymentService.createPayment(message);
            log.info("Payment successful for order = " + message.orderId());

        } else if (message.status() == OrderCreationStatus.CANCEL) {
            paymentService.deletePaymentByOrderId(message.orderId());
            log.info("Payment is deleted for order = " + message.orderId());
        }

        ack.acknowledge();
    }
}
