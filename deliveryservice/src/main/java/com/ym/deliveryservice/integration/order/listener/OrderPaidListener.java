package com.ym.deliveryservice.integration.order.listener;

import com.ym.deliveryservice.application.service.ShipmentService;
import com.ym.deliveryservice.integration.order.dto.request.OrderPaidRequestMessage;
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
public class OrderPaidListener {
 private final ShipmentService shipmentService;
    @KafkaListener(
            topics = "${kafka.service.delivery.order-paid-topic}",
            containerFactory = "orderPaidKafkaListenerContainerFactory"
    )
    public void consume(ConsumerRecord<String, OrderPaidRequestMessage> consumerRecord,
                        OrderPaidRequestMessage message,
                        Acknowledgment ack) {
        log.info("Received order paid message with ID: {}", message.getOrderId());
        ShipmentResponseDTO shipmentResponse = shipmentService.createShipment(new ShipmentRequestDTO(message.getOrderId().toString()));
        log.info("Created delivery with ID: {}", shipmentResponse.getId());

        ack.acknowledge();
        log.info("Order message successfully processed and acknowledged");
    }
}
