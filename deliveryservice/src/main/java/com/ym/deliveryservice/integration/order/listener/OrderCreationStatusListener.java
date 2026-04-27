package com.ym.deliveryservice.integration.order.listener;

import com.ym.deliveryservice.application.service.ShipmentService;
import com.ym.deliveryservice.domain.model.OrderId;
import com.ym.deliveryservice.integration.order.dto.message.OrderCreationStatus;
import com.ym.deliveryservice.integration.order.dto.message.OrderCreationStatusMessage;
import com.ym.deliveryservice.interfaces.dto.ShipmentRequestDTO;
import com.ym.deliveryservice.interfaces.dto.ShipmentResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreationStatusListener {
    private final ShipmentService shipmentService;

    @KafkaListener(
            topics = "${kafka.service.order.order-creation-status-topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "orderPaidKafkaListenerContainerFactory"
    )
    public void consume(OrderCreationStatusMessage message,
                        Acknowledgment ack) {
        OrderCreationStatus messageStatus = message.status();

        if (messageStatus == OrderCreationStatus.PAYMENT_SUCCESSFUL) {
            ShipmentResponseDTO shipmentResponse = shipmentService.createShipment(new ShipmentRequestDTO(message.orderId().toString()));
            log.info("Created delivery with ID: {}", shipmentResponse.getId());

        } else if (messageStatus == OrderCreationStatus.PAYMENT_FAILED) {
            shipmentService.deleteShipmentByOrderId(new OrderId(message.orderId().toString()));
            log.info("Shipment is deleted for order  = " + message.orderId());
        }

        ack.acknowledge();
        log.info("Order message successfully processed and acknowledged");
    }
}
