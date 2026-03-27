package com.ym.deliveryservice.interfaces.controller;

import com.ym.deliveryservice.application.service.ShipmentService;
import com.ym.deliveryservice.interfaces.dto.ShipmentResponseDTO;
import com.ym.deliveryservice.interfaces.dto.ShipmentUpdateDTO;
import com.ym.deliveryservice.interfaces.dto.ShipmentRequestDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CircuitBreaker(name = "shipmentControllerCircuitBreaker")
public class ShipmentController implements ShipmentControllerDoc {

  private final ShipmentService shipmentService;

  @Override
  public ResponseEntity<ShipmentResponseDTO> createShipment(@Valid @RequestBody ShipmentRequestDTO request) {
    ShipmentResponseDTO response = shipmentService.createShipment(request);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<ShipmentResponseDTO> getShipment(@PathVariable String id) {
    ShipmentResponseDTO response = shipmentService.getShipment(id);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<ShipmentResponseDTO> getShipmentByTrackingNumber(@PathVariable String trackingNumber) {
    ShipmentResponseDTO response = shipmentService.getShipmentByTrackingNumber(trackingNumber);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<List<ShipmentResponseDTO>> getAllShipments() {
    List<ShipmentResponseDTO> responses = shipmentService.getAllShipments();
    return ResponseEntity.ok(responses);
  }

  @Override
  public ResponseEntity<List<ShipmentResponseDTO>> getShipmentsByStatus(@PathVariable String status) {
    List<ShipmentResponseDTO> responses = shipmentService.getShipmentsByStatus(status);
    return ResponseEntity.ok(responses);
  }

  @Override
  public ResponseEntity<ShipmentResponseDTO> updateShipment(
          @PathVariable String id,
          @Valid @RequestBody ShipmentUpdateDTO updateDTO) {
    ShipmentResponseDTO response = shipmentService.updateShipment(id, updateDTO);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Void> deleteShipment(@PathVariable String id) {
    shipmentService.deleteShipment(id);
    return ResponseEntity.noContent().build();
  }
}