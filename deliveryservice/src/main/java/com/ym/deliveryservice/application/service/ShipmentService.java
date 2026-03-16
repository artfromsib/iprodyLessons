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

import java.time.LocalDateTime;
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

    if (shipmentRepository.findByTrackingNumber(new TrackingNumber(request.getTrackingNumber())).isPresent()) {
      throw new IllegalArgumentException("Shipment with tracking number " + request.getTrackingNumber() + " already exists");
    }

    ShipmentId shipmentId = new ShipmentId(UUID.randomUUID().toString());
    OrderId orderId = new OrderId(request.getOrderId());
    TrackingNumber trackingNumber = new TrackingNumber(request.getTrackingNumber());

    ShippingAddress address = new ShippingAddress(
            request.getStreet(),
            request.getCity(),
            request.getState(),
            request.getZipCode(),
            request.getCountry()
    );

    ShippingCost cost = new ShippingCost(request.getShippingCost(), request.getCurrency());

    LocalDateTime estimatedDelivery = request.getEstimatedDeliveryDate() != null ?
            request.getEstimatedDeliveryDate() : LocalDateTime.now().plusDays(5);

    Shipment shipment = Shipment.builder()
            .id(shipmentId)
            .orderId(orderId)
            .trackingNumber(trackingNumber)
            .shippingAddress(address)
            .status(DeliveryStatus.PENDING)
            .deliveryOption(request.getDeliveryOption())
            .shippingCost(cost)
            .createdAt(LocalDateTime.now())
            .estimatedDeliveryDate(estimatedDelivery)
            .build();

    Shipment savedShipment = shipmentRepository.save(shipment);
    log.info("Shipment created with ID: {}, Tracking: {}",
            savedShipment.getId().getValue(), savedShipment.getTrackingNumber().getValue());

    return ShipmentResponseDTO.fromDomain(savedShipment);
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