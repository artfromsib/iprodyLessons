package com.ym.paymentservice.interfaces.controller;

import com.ym.paymentservice.application.service.PaymentService;
import com.ym.paymentservice.domain.model.enums.PaymentStatus;
import com.ym.paymentservice.interfaces.dto.PayResponseDTO;
import com.ym.paymentservice.interfaces.dto.PaymentRequestDTO;
import com.ym.paymentservice.interfaces.dto.PaymentResponseDTO;
import com.ym.paymentservice.interfaces.dto.PaymentUpdateDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CircuitBreaker(name = "paymentControllerCircuitBreaker")
public class PaymentController implements PaymentControllerDoc {

  private final PaymentService paymentService;

  @Override
  public ResponseEntity<PayResponseDTO> payProcess(@RequestBody PaymentRequestDTO request) {
    try {
      PaymentResponseDTO payment = paymentService.payProcess(request);
      PayResponseDTO response = new PayResponseDTO(true, payment.getOrderId());
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(new PayResponseDTO(false, request.getOrderId()), HttpStatus.PAYMENT_REQUIRED);
    }
  }

  @Override
  public ResponseEntity<PaymentResponseDTO> createPayment(@RequestBody PaymentRequestDTO request) {
    PaymentResponseDTO response = paymentService.createPayment(request, PaymentStatus.PENDING);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<PaymentResponseDTO> getPayment(@PathVariable String id) {
    PaymentResponseDTO response = paymentService.getPayment(id);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
    List<PaymentResponseDTO> responses = paymentService.getAllPayments();
    return ResponseEntity.ok(responses);
  }

  @Override
  public ResponseEntity<PaymentResponseDTO> updatePayment(
          @PathVariable String id,
          @RequestBody PaymentUpdateDTO updateDTO) {
    PaymentResponseDTO response = paymentService.updatePayment(id, updateDTO);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Void> deletePayment(@PathVariable String id) {
    paymentService.deletePayment(id);
    return ResponseEntity.noContent().build();
  }
}