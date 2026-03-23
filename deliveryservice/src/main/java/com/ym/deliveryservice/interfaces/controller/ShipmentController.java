package com.ym.deliveryservice.interfaces.controller;

import com.ym.deliveryservice.application.service.ShipmentService;
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor
@Tag(name = "Контроллер доставки", description = "API для управления отправлениями и доставкой")
public class ShipmentController {

  private final ShipmentService shipmentService;

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
  public ResponseEntity<ShipmentResponseDTO> createShipment(
          @Parameter(description = "Данные для создания отправления", required = true)
          @Valid @RequestBody ShipmentRequestDTO request) {
    ShipmentResponseDTO response = shipmentService.createShipment(request);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

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
  public ResponseEntity<ShipmentResponseDTO> getShipment(
          @Parameter(description = "ID отправления", required = true, example = "ship_123e4567-e89b-12d3-a456-426614174000")
          @PathVariable String id) {
    ShipmentResponseDTO response = shipmentService.getShipment(id);
    return ResponseEntity.ok(response);
  }

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
  public ResponseEntity<ShipmentResponseDTO> getShipmentByTrackingNumber(
          @Parameter(description = "Трек-номер отправления", required = true, example = "TRK123456789")
          @PathVariable String trackingNumber) {
    ShipmentResponseDTO response = shipmentService.getShipmentByTrackingNumber(trackingNumber);
    return ResponseEntity.ok(response);
  }

  @Operation(
          summary = "Получить все отправления",
          description = "Возвращает список всех отправлений"
  )
  @ApiResponse(responseCode = "200", description = "Список отправлений успешно получен",
          content = @Content(schema = @Schema(implementation = ShipmentResponseDTO.class)))
  @GetMapping
  public ResponseEntity<List<ShipmentResponseDTO>> getAllShipments() {
    List<ShipmentResponseDTO> responses = shipmentService.getAllShipments();
    return ResponseEntity.ok(responses);
  }

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
  public ResponseEntity<List<ShipmentResponseDTO>> getShipmentsByStatus(
          @Parameter(description = "Статус отправления", required = true, example = "PENDING")
          @PathVariable String status) {
    List<ShipmentResponseDTO> responses = shipmentService.getShipmentsByStatus(status);
    return ResponseEntity.ok(responses);
  }

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
  public ResponseEntity<ShipmentResponseDTO> updateShipment(
          @Parameter(description = "ID отправления", required = true)
          @PathVariable String id,
          @Parameter(description = "Данные для обновления отправления", required = true)
          @Valid @RequestBody ShipmentUpdateDTO updateDTO) {
    ShipmentResponseDTO response = shipmentService.updateShipment(id, updateDTO);
    return ResponseEntity.ok(response);
  }

  @Operation(
          summary = "Удалить отправление",
          description = "Удаляет отправление по его идентификатору"
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "204", description = "Отправление успешно удалено"),
          @ApiResponse(responseCode = "404", description = "Отправление не найдено")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteShipment(
          @Parameter(description = "ID отправления", required = true)
          @PathVariable String id) {
    shipmentService.deleteShipment(id);
    return ResponseEntity.noContent().build();
  }
}