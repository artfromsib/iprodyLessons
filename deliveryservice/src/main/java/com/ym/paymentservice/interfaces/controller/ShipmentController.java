package com.ym.paymentservice.interfaces.controller;

import com.ym.paymentservice.application.service.ShipmentService;
import com.ym.paymentservice.interfaces.dto.ShipmentRequestDTO;
import com.ym.paymentservice.interfaces.dto.ShipmentResponseDTO;
import com.ym.paymentservice.interfaces.dto.ShipmentUpdateDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor
public class ShipmentController {

  private final ShipmentService shipmentService;

  @PostMapping
  public ResponseEntity<ShipmentResponseDTO> createShipment(@Valid @RequestBody ShipmentRequestDTO request) {
    ShipmentResponseDTO response = shipmentService.createShipment(request);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ShipmentResponseDTO> getShipment(@PathVariable String id) {
    ShipmentResponseDTO response = shipmentService.getShipment(id);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/tracking/{trackingNumber}")
  public ResponseEntity<ShipmentResponseDTO> getShipmentByTrackingNumber(@PathVariable String trackingNumber) {
    ShipmentResponseDTO response = shipmentService.getShipmentByTrackingNumber(trackingNumber);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<List<ShipmentResponseDTO>> getAllShipments() {
    List<ShipmentResponseDTO> responses = shipmentService.getAllShipments();
    return ResponseEntity.ok(responses);
  }

  @GetMapping("/status/{status}")
  public ResponseEntity<List<ShipmentResponseDTO>> getShipmentsByStatus(@PathVariable String status) {
    List<ShipmentResponseDTO> responses = shipmentService.getShipmentsByStatus(status);
    return ResponseEntity.ok(responses);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ShipmentResponseDTO> updateShipment(
          @PathVariable String id,
          @Valid @RequestBody ShipmentUpdateDTO updateDTO) {
    ShipmentResponseDTO response = shipmentService.updateShipment(id, updateDTO);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteShipment(@PathVariable String id) {
    shipmentService.deleteShipment(id);
    return ResponseEntity.noContent().build();
  }
}