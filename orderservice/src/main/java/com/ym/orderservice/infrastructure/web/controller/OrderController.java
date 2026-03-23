package com.ym.orderservice.infrastructure.web.controller;

import com.ym.orderservice.application.service.OrderService;
import com.ym.orderservice.domain.model.valueobject.OrderStatus;
import com.ym.orderservice.infrastructure.client.PaymentService;
import com.ym.orderservice.infrastructure.web.dto.OrderRequest;
import com.ym.orderservice.infrastructure.web.dto.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "API для управления заказами")
public class OrderController {

  private final OrderService orderService;
  private final PaymentService paymentService;
  @PostMapping
  @Operation(summary = "Создать новый заказ",
          description = "Создает заказ на основе переданных данных")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "Заказ успешно создан"),
          @ApiResponse(responseCode = "400", description = "Неверные данные запроса")
  })
  public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
    OrderResponse response = orderService.createOrder(request);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }
  @PostMapping("/pay/{orderId}")
  @Operation(summary = "Оплатить заказ",
          description = "Выполняет оплату существующего заказа")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Оплата успешно выполнена"),
          @ApiResponse(responseCode = "402", description = "Оплата не прошла"),
          @ApiResponse(responseCode = "404", description = "Заказ не найден")
  })
  public  ResponseEntity<?> payOrder(@PathVariable("orderId") UUID id) {
    try {
      OrderResponse response = orderService.getOrder(id);
      Optional<OrderResponse> order = paymentService.payOrder(response);
      if(order.isPresent()) {
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

  @GetMapping("/{id}")
  @Operation(summary = "Получить заказ по ID",
          description = "Возвращает информацию о заказе")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Заказ найден"),
          @ApiResponse(responseCode = "404", description = "Заказ не найден",
                  content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
  })
  public ResponseEntity<?> getOrder(@PathVariable UUID id) {
    try {
      OrderResponse response = orderService.getOrder(id);
      return ResponseEntity.ok(response);
    } catch (RuntimeException e) {
      return handleException(e, id);
    }
  }


  @GetMapping
  @Operation(summary = "Получить все заказы",
          description = "Возвращает список всех заказов")
  @ApiResponse(responseCode = "200", description = "Список заказов успешно получен")
  public ResponseEntity<List<OrderResponse>> getAllOrders() {
    List<OrderResponse> responses = orderService.getAllOrders();
    return ResponseEntity.ok(responses);
  }

  @GetMapping("/customer/{customerId}")
  @Operation(summary = "Получить заказы клиента",
          description = "Возвращает список заказов конкретного клиента")
  @ApiResponse(responseCode = "200", description = "Список заказов успешно получен")
  public ResponseEntity<List<OrderResponse>> getOrdersByCustomer(@PathVariable Long customerId) {
    List<OrderResponse> responses = orderService.getOrdersByCustomer(customerId);
    return ResponseEntity.ok(responses);
  }

  @PatchMapping("/{id}/status")
  @Operation(summary = "Обновить статус заказа",
          description = "Изменяет статус существующего заказа")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Статус успешно обновлен"),
          @ApiResponse(responseCode = "404", description = "Заказ не найден")
  })
  public ResponseEntity<OrderResponse> updateOrderStatus(
          @PathVariable UUID id,
          @RequestParam OrderStatus status) {
    OrderResponse response = orderService.updateOrderStatus(id, status);
    return ResponseEntity.ok(response);
  }

  @PatchMapping("/{id}/notes")
  @Operation(summary = "Добавить заметку к заказу",
          description = "Добавляет текстовую заметку к заказу")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Заметка добавлена"),
          @ApiResponse(responseCode = "404", description = "Заказ не найден")
  })
  public ResponseEntity<OrderResponse> addOrderNotes(
          @PathVariable UUID id,
          @RequestBody String notes) {
    OrderResponse response = orderService.addOrderNotes(id, notes);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Удалить заказ",
          description = "Удаляет существующий заказ")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "204", description = "Заказ успешно удален"),
          @ApiResponse(responseCode = "404", description = "Заказ не найден")
  })
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