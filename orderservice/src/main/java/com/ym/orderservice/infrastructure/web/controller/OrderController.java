package com.ym.orderservice.infrastructure.web.controller;

import com.ym.orderservice.application.service.OrderService;
import com.ym.orderservice.domain.model.aggregate.Order;
import com.ym.orderservice.domain.model.valueobject.OrderStatus;
import com.ym.orderservice.infrastructure.client.PaymentService;
import com.ym.orderservice.infrastructure.web.dto.OrderRequest;
import com.ym.orderservice.infrastructure.web.dto.OrderResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CircuitBreaker(name = "orderControllerCircuitBreaker")
public class OrderController implements OrderControllerDoc {

    private final OrderService orderService;
    private final PaymentService paymentService;

    @Override
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        Order response = orderService.createOrder(request);
        return new ResponseEntity<>(OrderResponse.fromDomain(response), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> payOrder(@PathVariable("orderId") UUID id) {
        try {
            OrderResponse response = orderService.getOrder(id);
            Optional<OrderResponse> order = paymentService.payOrder(response);
            if (order.isPresent()) {
                OrderResponse updatedOrder = orderService.updateOrderStatus(id, OrderStatus.PAID);
                return ResponseEntity.ok(updatedOrder);
            } else {
                OrderResponse updatedOrder = orderService.updateOrderStatus(id, OrderStatus.PAYMENT_FAILED);
                return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(updatedOrder);
            }
        } catch (RuntimeException e) {
            return handleException(e, id);
        }
    }

    @Override
    public ResponseEntity<?> getOrder(@PathVariable UUID id) {
        try {
            OrderResponse response = orderService.getOrder(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return handleException(e, id);
        }
    }

    @Override
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> responses = orderService.getAllOrders();
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomer(@PathVariable Long customerId) {
        List<OrderResponse> responses = orderService.getOrdersByCustomer(customerId);
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable UUID id,
            @RequestParam OrderStatus status) {
        OrderResponse response = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<OrderResponse> addOrderNotes(
            @PathVariable UUID id,
            @RequestBody String notes) {
        OrderResponse response = orderService.addOrderNotes(id, notes);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<ProblemDetail> handleException(RuntimeException e, UUID id) {
        if (e instanceof NoSuchElementException) {
            return createNotFoundResponse(id);
        }
        if (e instanceof IllegalArgumentException) {
            return createBadRequestResponse(e.getMessage());
        }
        return createInternalErrorResponse(e.getMessage());
    }

    private ResponseEntity<ProblemDetail> createNotFoundResponse(UUID id) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "Order with id " + id + " not found"
        );
        pd.setTitle("Order Not Found");
        pd.setProperty("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd);
    }

    private ResponseEntity<ProblemDetail> createBadRequestResponse(String message) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
        pd.setTitle("Bad Request");
        pd.setProperty("timestamp", LocalDateTime.now());
        return ResponseEntity.badRequest().body(pd);
    }

    private ResponseEntity<ProblemDetail> createInternalErrorResponse(String message) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, message);
        pd.setTitle("Internal Error");
        pd.setProperty("timestamp", LocalDateTime.now());
        return ResponseEntity.internalServerError().body(pd);
    }
}