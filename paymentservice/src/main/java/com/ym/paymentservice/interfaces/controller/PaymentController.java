package com.ym.paymentservice.interfaces.controller;


import com.ym.paymentservice.application.service.PaymentService;
import com.ym.paymentservice.interfaces.dto.PaymentRequestDTO;
import com.ym.paymentservice.interfaces.dto.PaymentResponseDTO;
import com.ym.paymentservice.interfaces.dto.PaymentUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

  private final PaymentService paymentService;

  @PostMapping
  public ResponseEntity<PaymentResponseDTO> createPayment(@RequestBody PaymentRequestDTO request) {
    PaymentResponseDTO response = paymentService.createPayment(request);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PaymentResponseDTO> getPayment(@PathVariable String id) {
    PaymentResponseDTO response = paymentService.getPayment(id);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
    List<PaymentResponseDTO> responses = paymentService.getAllPayments();
    return ResponseEntity.ok(responses);
  }

  @PutMapping("/{id}")
  public ResponseEntity<PaymentResponseDTO> updatePayment(
          @PathVariable String id,
          @RequestBody PaymentUpdateDTO updateDTO) {
    PaymentResponseDTO response = paymentService.updatePayment(id, updateDTO);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePayment(@PathVariable String id) {
    paymentService.deletePayment(id);
    return ResponseEntity.noContent().build();
  }
}