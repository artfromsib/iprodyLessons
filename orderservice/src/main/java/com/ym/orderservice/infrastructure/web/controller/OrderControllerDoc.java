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
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Order Management", description = "API для управления заказами")
@RequestMapping("/api/orders")
public interface OrderControllerDoc {

    @Operation(summary = "Создать новый заказ",
            description = "Создает заказ на основе переданных данных")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Заказ успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса")
    })
    @PostMapping
    ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request);

    @Operation(summary = "Оплатить заказ",
            description = "Выполняет оплату существующего заказа")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Оплата успешно выполнена"),
            @ApiResponse(responseCode = "402", description = "Оплата не прошла"),
            @ApiResponse(responseCode = "404", description = "Заказ не найден")
    })
    @PostMapping("/pay/{orderId}")
    ResponseEntity<?> payOrder(@PathVariable("orderId") UUID id);

    @Operation(summary = "Получить заказ по ID",
            description = "Возвращает информацию о заказе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Заказ найден"),
            @ApiResponse(responseCode = "404", description = "Заказ не найден",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{id}")
    ResponseEntity<?> getOrder(@PathVariable UUID id);

    @Operation(summary = "Получить все заказы",
            description = "Возвращает список всех заказов")
    @ApiResponse(responseCode = "200", description = "Список заказов успешно получен")
    @GetMapping
    ResponseEntity<List<OrderResponse>> getAllOrders();

    @Operation(summary = "Получить заказы клиента",
            description = "Возвращает список заказов конкретного клиента")
    @ApiResponse(responseCode = "200", description = "Список заказов успешно получен")
    @GetMapping("/customer/{customerId}")
    ResponseEntity<List<OrderResponse>> getOrdersByCustomer(@PathVariable Long customerId);

    @Operation(summary = "Обновить статус заказа",
            description = "Изменяет статус существующего заказа")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Заказ не найден")
    })
    @PatchMapping("/{id}/status")
    ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable UUID id,
            @RequestParam OrderStatus status);

    @Operation(summary = "Добавить заметку к заказу",
            description = "Добавляет текстовую заметку к заказу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Заметка добавлена"),
            @ApiResponse(responseCode = "404", description = "Заказ не найден")
    })
    @PatchMapping("/{id}/notes")
    ResponseEntity<OrderResponse> addOrderNotes(
            @PathVariable UUID id,
            @RequestBody String notes);

    @Operation(summary = "Удалить заказ",
            description = "Удаляет существующий заказ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Заказ успешно удален"),
            @ApiResponse(responseCode = "404", description = "Заказ не найден")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteOrder(@PathVariable UUID id);
}