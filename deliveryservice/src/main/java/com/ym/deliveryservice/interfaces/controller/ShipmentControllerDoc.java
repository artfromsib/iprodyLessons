package com.ym.deliveryservice.interfaces.controller;

import com.ym.deliveryservice.interfaces.dto.ShipmentResponseDTO;
import com.ym.deliveryservice.interfaces.dto.ShipmentUpdateDTO;
import com.ym.deliveryservice.interfaces.dto.ShipmentRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Контроллер доставки", description = "API для управления отправлениями и доставкой")
@RequestMapping("/api/shipments")
public interface ShipmentControllerDoc {

  @Operation(
          summary = "Создать отправление",
          description = "Создает новое отправление для доставки заказа"
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "Отправление успешно создано",
                  content = @Content(schema = @Schema(implementation = ShipmentResponseDTO.class))),
          @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
          @ApiResponse(responseCode = "404", description = "Заказ не найден")
  })
  @PostMapping
  ResponseEntity<ShipmentResponseDTO> createShipment(
          @Parameter(description = "Данные для создания отправления", required = true)
          @Valid @RequestBody ShipmentRequestDTO request);

  @Operation(
          summary = "Получить отправление по ID",
          description = "Возвращает информацию об отправлении по его идентификатору"
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Отправление найдено",
                  content = @Content(schema = @Schema(implementation = ShipmentResponseDTO.class))),
          @ApiResponse(responseCode = "404", description = "Отправление не найдено")
  })
  @GetMapping("/{id}")
  ResponseEntity<ShipmentResponseDTO> getShipment(
          @Parameter(description = "ID отправления", required = true, example = "ship_123e4567-e89b-12d3-a456-426614174000")
          @PathVariable String id);

  @Operation(
          summary = "Получить отправление по трек-номеру",
          description = "Возвращает информацию об отправлении по его трек-номеру"
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Отправление найдено",
                  content = @Content(schema = @Schema(implementation = ShipmentResponseDTO.class))),
          @ApiResponse(responseCode = "404", description = "Отправление не найдено")
  })
  @GetMapping("/tracking/{trackingNumber}")
  ResponseEntity<ShipmentResponseDTO> getShipmentByTrackingNumber(
          @Parameter(description = "Трек-номер отправления", required = true, example = "TRK123456789")
          @PathVariable String trackingNumber);

  @Operation(
          summary = "Получить все отправления",
          description = "Возвращает список всех отправлений"
  )
  @ApiResponse(responseCode = "200", description = "Список отправлений успешно получен",
          content = @Content(schema = @Schema(implementation = ShipmentResponseDTO.class)))
  @GetMapping
  ResponseEntity<List<ShipmentResponseDTO>> getAllShipments();

  @Operation(
          summary = "Получить отправления по статусу",
          description = "Возвращает список отправлений с указанным статусом"
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Список отправлений успешно получен",
                  content = @Content(schema = @Schema(implementation = ShipmentResponseDTO.class))),
          @ApiResponse(responseCode = "400", description = "Неверный статус")
  })
  @GetMapping("/status/{status}")
  ResponseEntity<List<ShipmentResponseDTO>> getShipmentsByStatus(
          @Parameter(description = "Статус отправления", required = true, example = "PENDING")
          @PathVariable String status);

  @Operation(
          summary = "Обновить отправление",
          description = "Обновляет информацию о существующем отправлении"
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Отправление успешно обновлено",
                  content = @Content(schema = @Schema(implementation = ShipmentResponseDTO.class))),
          @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
          @ApiResponse(responseCode = "404", description = "Отправление не найдено")
  })
  @PutMapping("/{id}")
  ResponseEntity<ShipmentResponseDTO> updateShipment(
          @Parameter(description = "ID отправления", required = true)
          @PathVariable String id,
          @Parameter(description = "Данные для обновления отправления", required = true)
          @Valid @RequestBody ShipmentUpdateDTO updateDTO);

  @Operation(
          summary = "Удалить отправление",
          description = "Удаляет отправление по его идентификатору"
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "204", description = "Отправление успешно удалено"),
          @ApiResponse(responseCode = "404", description = "Отправление не найдено")
  })
  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteShipment(
          @Parameter(description = "ID отправления", required = true)
          @PathVariable String id);
}