package com.ym.deliveryservice.application.service;

import com.ym.deliveryservice.domain.model.*;
import com.ym.deliveryservice.interfaces.dto.ShipmentResponseDTO;
import com.ym.deliveryservice.interfaces.dto.ShipmentUpdateDTO;
import com.ym.deliveryservice.interfaces.exception.ShipmentNotFoundException;
import com.ym.deliveryservice.domain.model.*;
import com.ym.deliveryservice.domain.repository.ShipmentRepository;
import com.ym.deliveryservice.interfaces.dto.ShipmentRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

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

        return ShipmentResponseDTO.fromDomain(savedShipment);
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

    @Transactional(readOnly = true)
    public ShipmentResponseDTO getShipment(String id) {
        Shipment shipment = findShipmentById(id);
        return ShipmentResponseDTO.fromDomain(shipment);
    }

    @Transactional(readOnly = true)
    public ShipmentResponseDTO getShipmentByTrackingNumber(String trackingNumber) {
        TrackingNumber tn = new TrackingNumber(trackingNumber);
        Shipment shipment = shipmentRepository.findByTrackingNumber(tn)
                .orElseThrow(() -> new ShipmentNotFoundException(
                        "Shipment not found with tracking number: " + trackingNumber));
        return ShipmentResponseDTO.fromDomain(shipment);
    }

    @Transactional(readOnly = true)
    public List<ShipmentResponseDTO> getAllShipments() {
        return shipmentRepository.findAll().stream()
                .map(ShipmentResponseDTO::fromDomain)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ShipmentResponseDTO> getShipmentsByStatus(String status) {
        DeliveryStatus deliveryStatus;
        try {
            deliveryStatus = DeliveryStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }

        return shipmentRepository.findByStatus(deliveryStatus).stream()
                .map(ShipmentResponseDTO::fromDomain)
                .collect(Collectors.toList());
    }

    @Transactional
    public ShipmentResponseDTO updateShipment(String id, ShipmentUpdateDTO updateDTO) {
        Shipment shipment = findShipmentById(id);

        if (updateDTO.getStatus() != null) {
            switch (updateDTO.getStatus().toUpperCase()) {
                case "WITH_COURIER":
                    shipment.assignToCourier();
                    break;
                case "IN_TRANSIT":
                    shipment.markInTransit();
                    break;
                case "DELIVERED":
                    shipment.markAsDelivered();
                    break;
                default:
                    throw new IllegalArgumentException("Invalid status transition");
            }
        }

        if (updateDTO.getTrackingNumber() != null) {
            shipment.updateTrackingNumber(updateDTO.getTrackingNumber());
        }

        if (updateDTO.getEstimatedDeliveryDate() != null) {
            shipment.setEstimatedDeliveryDate(updateDTO.getEstimatedDeliveryDate());
        }

        Shipment updatedShipment = shipmentRepository.save(shipment);
        log.info("Shipment updated with ID: {}", updatedShipment.getId().getValue());

        return ShipmentResponseDTO.fromDomain(updatedShipment);
    }

    @Transactional
    public void deleteShipment(String id) {
        ShipmentId shipmentId = new ShipmentId(id);
        if (!shipmentRepository.existsById(shipmentId)) {
            throw new ShipmentNotFoundException("Shipment not found with id: " + id);
        }
        shipmentRepository.deleteById(shipmentId);
        log.info("Shipment deleted with ID: {}", id);
    }

    private Shipment findShipmentById(String id) {
        ShipmentId shipmentId = new ShipmentId(id);
        return shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found with id: " + id));
    }
}