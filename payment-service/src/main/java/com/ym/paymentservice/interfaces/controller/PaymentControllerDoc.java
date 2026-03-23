package com.ym.paymentservice.interfaces.controller;

import com.ym.paymentservice.interfaces.dto.PayResponseDTO;
import com.ym.paymentservice.interfaces.dto.PaymentRequestDTO;
import com.ym.paymentservice.interfaces.dto.PaymentResponseDTO;
import com.ym.paymentservice.interfaces.dto.PaymentUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Контроллер платежей", description = "API для управления платежами")
@RequestMapping("/api/payments")
public interface PaymentControllerDoc {

  @Operation(
          summary = "Обработать платеж",
          description = "Выполняет обработку платежа для заказа"
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Платеж успешно обработан",
                  content = @Content(schema = @Schema(implementation = PayResponseDTO.class))),
          @ApiResponse(responseCode = "402", description = "Требуется оплата - недостаточно средств",
                  content = @Content(schema = @Schema(implementation = PayResponseDTO.class)))
  })
  @PostMapping("/pay")
  ResponseEntity<PayResponseDTO> payProcess(
          @Parameter(description = "Данные запроса на оплату", required = true)
          @RequestBody PaymentRequestDTO request);

  @Operation(
          summary = "Создать платеж",
          description = "Создает новую запись о платеже"
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "Платеж успешно создан",
                  content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))),
          @ApiResponse(responseCode = "400", description = "Неверные входные данные")
  })
  @PostMapping
  ResponseEntity<PaymentResponseDTO> createPayment(
          @Parameter(description = "Данные запроса на создание платежа", required = true)
          @RequestBody PaymentRequestDTO request);

  @Operation(
          summary = "Получить платеж по ID",
          description = "Возвращает информацию о платеже по его идентификатору"
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Платеж найден",
                  content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))),
          @ApiResponse(responseCode = "404", description = "Платеж не найден")
  })
  @GetMapping("/{id}")
  ResponseEntity<PaymentResponseDTO> getPayment(
          @Parameter(description = "ID платежа", required = true)
          @PathVariable String id);

  @Operation(
          summary = "Получить все платежи",
          description = "Возвращает список всех платежей"
  )
  @ApiResponse(responseCode = "200", description = "Список платежей успешно получен",
          content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class)))
  @GetMapping
  ResponseEntity<List<PaymentResponseDTO>> getAllPayments();

  @Operation(
          summary = "Обновить платеж",
          description = "Обновляет информацию о существующем платеже"
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Платеж успешно обновлен",
                  content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))),
          @ApiResponse(responseCode = "404", description = "Платеж не найден")
  })
  @PutMapping("/{id}")
  ResponseEntity<PaymentResponseDTO> updatePayment(
          @Parameter(description = "ID платежа", required = true)
          @PathVariable String id,
          @Parameter(description = "Данные для обновления платежа", required = true)
          @RequestBody PaymentUpdateDTO updateDTO);

  @Operation(
          summary = "Удалить платеж",
          description = "Удаляет платеж по его идентификатору"
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "204", description = "Платеж успешно удален"),
          @ApiResponse(responseCode = "404", description = "Платеж не найден")
  })
  @DeleteMapping("/{id}")
  ResponseEntity<Void> deletePayment(
          @Parameter(description = "ID платежа", required = true)
          @PathVariable String id);
}