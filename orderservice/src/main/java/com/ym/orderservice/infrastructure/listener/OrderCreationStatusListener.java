package com.ym.orderservice.infrastructure.listener;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.ym.orderservice.application.service.OrderService;
import com.ym.orderservice.domain.model.valueobject.OrderStatus;
import com.ym.orderservice.infrastructure.web.dto.message.OrderCreationStatus;
import com.ym.orderservice.infrastructure.web.dto.message.OrderCreationStatusMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.EnumSet;


@RequiredArgsConstructor
@Slf4j
@Component
public class OrderCreationStatusListener {


    private static final EnumSet<OrderCreationStatus> ERROR_STATUS =
        EnumSet.of(OrderCreationStatus.DELIVERY_FAILED, OrderCreationStatus.PAYMENT_FAILED);

    private final OrderService orderService;

    @KafkaListener(
        topics = "${kafka.service.order.order-creation-status-topic}",
        groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(OrderCreationStatusMessage message,
                        Acknowledgment ack) throws JsonProcessingException {
        OrderCreationStatus messageStatus = message.status();

        if (messageStatus == OrderCreationStatus.PAYMENT_SUCCESSFUL) {
            orderService.changeOrderStatus(message.orderId(), OrderStatus.PAID);
            log.info("Received status message '%s' and changed order status to '%s'"
                .formatted(messageStatus, OrderStatus.PAID));

        } else if (messageStatus == OrderCreationStatus.DELIVERY_CREATED) {
            orderService.changeOrderStatus(message.orderId(), OrderStatus.SHIPPED);
            log.info("Received status message '%s' and changed order status to '%s'"
                .formatted(messageStatus, OrderStatus.SHIPPED));

        } else if (ERROR_STATUS.contains(messageStatus)) {
            orderService.cancelAppointment(message.orderId());
            log.info("Received status message '%s' and cancelled order".formatted(messageStatus));
        }

        ack.acknowledge();
    }
}
