package com.ym.orderservice.infrastructure.web.controller;

import com.ym.orderservice.application.service.OrderService;
import com.ym.orderservice.domain.model.valueobject.OrderStatus;
import com.ym.orderservice.infrastructure.web.dto.OrderRequest;
import com.ym.orderservice.infrastructure.web.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
    OrderResponse response = orderService.createOrder(request);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getOrder(@PathVariable UUID id) {
    try {
      OrderResponse response = orderService.getOrder(id);
      return ResponseEntity.ok(response);
    } catch (RuntimeException e) {

      ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
              HttpStatus.NOT_FOUND,
              "Order with id " + id + " not found"
      );
      problemDetail.setTitle("Order Not Found");
      problemDetail.setProperty("timestamp", LocalDateTime.now());
      problemDetail.setProperty("path", "/api/orders/" + id);

      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(problemDetail);
    }
  }


  @GetMapping
  public ResponseEntity<List<OrderResponse>> getAllOrders() {
    List<OrderResponse> responses = orderService.getAllOrders();
    return ResponseEntity.ok(responses);
  }

  @GetMapping("/customer/{customerId}")
  public ResponseEntity<List<OrderResponse>> getOrdersByCustomer(@PathVariable Long customerId) {
    List<OrderResponse> responses = orderService.getOrdersByCustomer(customerId);
    return ResponseEntity.ok(responses);
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<OrderResponse> updateOrderStatus(
          @PathVariable UUID id,
          @RequestParam OrderStatus status) {
    OrderResponse response = orderService.updateOrderStatus(id, status);
    return ResponseEntity.ok(response);
  }

  @PatchMapping("/{id}/notes")
  public ResponseEntity<OrderResponse> addOrderNotes(
          @PathVariable UUID id,
          @RequestBody String notes) {
    OrderResponse response = orderService.addOrderNotes(id, notes);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
    orderService.deleteOrder(id);
    return ResponseEntity.noContent().build();
  }
}