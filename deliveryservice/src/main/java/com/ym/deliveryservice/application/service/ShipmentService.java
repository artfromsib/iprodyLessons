package com.ym.deliveryservice.application.service;

import com.ym.deliveryservice.domain.model.*;
import com.ym.deliveryservice.integration.order.dto.message.OrderCreationStatus;
import com.ym.deliveryservice.integration.order.dto.message.OrderCreationStatusMessage;
import com.ym.deliveryservice.interfaces.dto.ShipmentResponseDTO;
import com.ym.deliveryservice.interfaces.exception.ShipmentNotFoundException;
import com.ym.deliveryservice.domain.repository.ShipmentRepository;
import com.ym.deliveryservice.interfaces.dto.ShipmentRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    @Value("${kafka.service.order.order-creation-status-topic}")
    private String deliveryCreationTopic;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public ShipmentResponseDTO createShipment(ShipmentRequestDTO request) {

        if (request.getOrderId() == null || request.getOrderId().isBlank()) {
            throw new IllegalArgumentException("Order ID is required");
        }

        OrderId orderId = new OrderId(request.getOrderId());

        if (shipmentRepository.findByOrderId(orderId).isPresent()) {
            throw new IllegalArgumentException("Shipment already exists for order: " + request.getOrderId());
        }

        ShipmentId shipmentId = new ShipmentId(UUID.randomUUID().toString());


        TrackingNumber trackingNumber;
        if (request.getTrackingNumber() != null && !request.getTrackingNumber().isBlank()) {

            if (shipmentRepository.findByTrackingNumber(new TrackingNumber(request.getTrackingNumber())).isPresent()) {
                throw new IllegalArgumentException("Shipment with tracking number " + request.getTrackingNumber() + " already exists");
            }
            trackingNumber = new TrackingNumber(request.getTrackingNumber());
        } else {
            trackingNumber = generateTrackingNumber();
        }


        ShippingAddress address = null;
        if (request.getStreet() == null || request.getStreet().isBlank()) {

            address = new ShippingAddress(
                    "Default Street",
                    "Default City",
                    "Default State",
                    "00000",
                    "Default Country"
            );
        } else {
            address = new ShippingAddress(
                    request.getStreet(),
                    request.getCity(),
                    request.getState(),
                    request.getZipCode(),
                    request.getCountry()
            );
        }
        ShippingCost cost;
        if (request.getShippingCost() != null) {
            Currency currency = request.getCurrency() != null ? request.getCurrency() : Currency.getInstance("USD");
            cost = new ShippingCost(request.getShippingCost(), currency);
        } else {
            cost = new ShippingCost(BigDecimal.ZERO, Currency.getInstance("USD"));
        }

        LocalDateTime estimatedDelivery = request.getEstimatedDeliveryDate() != null ?
                request.getEstimatedDeliveryDate() : LocalDateTime.now().plusDays(5);

        DeliveryOption deliveryOption = request.getDeliveryOption() != null ?
                request.getDeliveryOption() : DeliveryOption.POST;

        Shipment shipment = Shipment.builder()
                .id(shipmentId)
                .orderId(orderId)
                .trackingNumber(trackingNumber)
                .shippingAddress(address)
                .status(DeliveryStatus.PENDING)
                .deliveryOption(deliveryOption)
                .shippingCost(cost)
                .createdAt(LocalDateTime.now())
                .estimatedDeliveryDate(estimatedDelivery)
                .build();

        Shipment savedShipment = shipmentRepository.save(shipment);
        log.info("Shipment created with ID: {}, Order ID: {}, Tracking: {}",
                savedShipment.getId().getValue(),
                savedShipment.getOrderId().getValue(),
                savedShipment.getTrackingNumber().getValue());
        sendStatusMessage(orderId, savedShipment != null);
        return ShipmentResponseDTO.fromDomain(savedShipment);
    }
    private void sendStatusMessage(OrderId orderId,
                                   boolean savedShipment) {
        var statusMessage = OrderCreationStatusMessage.builder()
                .orderId(UUID.fromString(orderId.getValue()))
                .status(savedShipment ? OrderCreationStatus.DELIVERY_CREATED : OrderCreationStatus.DELIVERY_FAILED)
                .build();

        kafkaTemplate.send(deliveryCreationTopic, statusMessage);
        log.info("Sent delivery creation message to Kafka for orderID: {}" , orderId);
    }

    private TrackingNumber generateTrackingNumber() {
        String trackingNumber;
        do {
            String timestamp = String.valueOf(System.currentTimeMillis()).substring(8);
            String random = UUID.randomUUID().toString().substring(0, 5).toUpperCase();
            trackingNumber = "SHIP" + timestamp + random;
        } while (shipmentRepository.findByTrackingNumber(new TrackingNumber(trackingNumber)).isPresent());

        return new TrackingNumber(trackingNumber);
    }




    @Transactional
    public void deleteShipmentByOrderId(OrderId orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("OrderId cannot be null or blank");
        }

        Optional<Shipment> shipmentOptional = shipmentRepository.findByOrderId(orderId);

        if (shipmentOptional.isEmpty()) {
            log.warn("Shipment not found for orderId: {}", orderId);
            throw new ShipmentNotFoundException("Shipment not found for orderId: " + orderId);
        }

        Shipment shipment = shipmentOptional.get();
        ShipmentId shipmentId = shipment.getId();
        shipmentRepository.deleteById(shipmentId);

        log.info("Shipment deleted successfully for orderId: {}. Shipment ID: {}",
                orderId, shipmentId.getValue());
    }

    private Shipment findShipmentById(String id) {
        ShipmentId shipmentId = new ShipmentId(id);
        return shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found with id: " + id));
    }

}