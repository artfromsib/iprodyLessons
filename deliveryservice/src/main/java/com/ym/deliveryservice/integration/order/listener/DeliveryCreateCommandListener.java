package com.ym.deliveryservice.integration.order.listener;

import com.ym.deliveryservice.application.service.ShipmentService;
import com.ym.deliveryservice.domain.model.OrderId;
import com.ym.deliveryservice.integration.order.dto.message.OrderCreationStatus;
import com.ym.deliveryservice.integration.order.dto.message.OrderCreationStatusMessage;
import com.ym.deliveryservice.integration.order.dto.request.DeliveryCreateRequestMessage;
import com.ym.deliveryservice.interfaces.dto.ShipmentRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeliveryCreateCommandListener {
    private final ShipmentService shipmentService;

    @KafkaListener(
            topics = "${kafka.service.delivery.delivery-create-request}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void deliveryCreateRequest(OrderCreationStatusMessage message) {
        log.info("Received delivery creation request for orderId: {}", message);
        shipmentService.createShipment(new ShipmentRequestDTO(message.orderId().toString()));
    }

    @KafkaListener(
            topics = "${kafka.service.delivery.delivery-delete-request}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void deliveryDeleteRequest(DeliveryCreateRequestMessage message) {
        log.info("Received delivery deletion request for orderId: {}", message.orderId());
        shipmentService.deleteShipmentByOrderId(new OrderId(message.orderId().toString()));
    }
}
