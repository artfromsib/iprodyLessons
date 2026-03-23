package com.ym.orderservice.infrastructure.web.dto;

import com.ym.orderservice.domain.model.aggregate.Order;
import com.ym.orderservice.domain.model.entity.OrderItem;
import com.ym.orderservice.domain.model.valueobject.Address;
import com.ym.orderservice.domain.model.valueobject.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@Schema(description = "Ответ с данными заказа")
public class OrderResponse {

  @Schema(description = "Уникальный идентификатор заказа",
          example = "123e4567-e89b-12d3-a456-426614174000",
          accessMode = Schema.AccessMode.READ_ONLY)
  private UUID id;

  @Schema(description = "Информация о клиенте",
          accessMode = Schema.AccessMode.READ_ONLY)
  private CustomerDto customer;

  @Schema(description = "Текущий статус заказа",
          example = "PAID",
          allowableValues = {"PENDING", "PAID", "PAYMENT_FAILED", "CANCELLED", "SHIPPED", "DELIVERED"},
          accessMode = Schema.AccessMode.READ_ONLY)
  private OrderStatus status;

  @Schema(description = "Общая сумма заказа",
          example = "1250.50",
          minimum = "0",
          accessMode = Schema.AccessMode.READ_ONLY)
  private BigDecimal totalAmount;

  @Schema(description = "Адрес доставки",
          accessMode = Schema.AccessMode.READ_ONLY)
  private AddressDto shippingAddress;

  @Schema(description = "Список товаров в заказе",
          accessMode = Schema.AccessMode.READ_ONLY)
  private List<OrderItemDto> items;

  @Schema(description = "Дата создания заказа",
          example = "2024-01-15T10:30:00",
          accessMode = Schema.AccessMode.READ_ONLY)
  private LocalDateTime createdAt;

  @Schema(description = "Дата последнего обновления заказа",
          example = "2024-01-15T14:45:30",
          accessMode = Schema.AccessMode.READ_ONLY)
  private LocalDateTime updatedAt;

  @Schema(description = "Дополнительные заметки к заказу",
          example = "Клиент просил перезвонить перед доставкой",
          accessMode = Schema.AccessMode.READ_ONLY)
  private String notes;

  @Data
  @Builder
  @Schema(description = "Информация о клиенте (расширенная)")
  public static class CustomerDto {

    @Schema(description = "ID клиента",
            example = "12345",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Полное имя клиента",
            example = "Иван Иванов",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String fullName;

    @Schema(description = "Email адрес",
            example = "ivan.ivanov@example.com",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String email;

    @Schema(description = "Номер телефона (сырой формат)",
            example = "+79991234567",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String phone;

    @Schema(description = "Номер телефона (отформатированный)",
            example = "+7 (999) 123-45-67",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String formattedPhone;

    @Schema(description = "Адрес клиента",
            accessMode = Schema.AccessMode.READ_ONLY)
    private AddressDto address;
  }

  @Data
  @Builder
  @Schema(description = "Адресная информация (расширенная)")
  public static class AddressDto {

    @Schema(description = "Улица и номер дома",
            example = "ул. Тверская, д. 15",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String street;

    @Schema(description = "Город",
            example = "Москва",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String city;

    @Schema(description = "Регион/Область",
            example = "Московская область",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String state;

    @Schema(description = "Почтовый индекс",
            example = "123456",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String zipCode;

    @Schema(description = "Страна",
            example = "Россия",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String country;

    @Schema(description = "Адрес в отформатированном виде",
            example = "ул. Тверская, д. 15, Москва, Московская область 123456, Россия",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String formattedAddress;
  }

  @Data
  @Builder
  @Schema(description = "Информация о товаре в заказе")
  public static class OrderItemDto {

    @Schema(description = "ID товара",
            example = "98765",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long productId;

    @Schema(description = "Название товара",
            example = "Смартфон iPhone 15 Pro",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String productName;

    @Schema(description = "Цена за единицу",
            example = "999.99",
            minimum = "0",
            accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal price;

    @Schema(description = "Количество",
            example = "2",
            minimum = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer quantity;

    @Schema(description = "Сумма по позиции (price * quantity)",
            example = "1999.98",
            minimum = "0",
            accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal subtotal;
  }

  public static OrderResponse fromDomain(Order order) {
    AddressDto customerAddressDto = AddressDto.builder()
            .street(order.getCustomer().getAddress().getStreet())
            .city(order.getCustomer().getAddress().getCity())
            .state(order.getCustomer().getAddress().getState())
            .zipCode(order.getCustomer().getAddress().getZipCode())
            .country(order.getCustomer().getAddress().getCountry())
            .formattedAddress(formatAddress(order.getCustomer().getAddress()))
            .build();

    CustomerDto customerDto = CustomerDto.builder()
            .id(order.getCustomer().getId())
            .fullName(order.getCustomer().getFullName())
            .email(order.getCustomer().getEmail())
            .phone(order.getCustomer().getPhone())
            .formattedPhone(order.getCustomer().getFormattedPhone())
            .address(customerAddressDto)
            .build();

    AddressDto shippingAddressDto = AddressDto.builder()
            .street(order.getShippingAddress().getStreet())
            .city(order.getShippingAddress().getCity())
            .state(order.getShippingAddress().getState())
            .zipCode(order.getShippingAddress().getZipCode())
            .country(order.getShippingAddress().getCountry())
            .formattedAddress(formatAddress(order.getShippingAddress()))
            .build();

    List<OrderItemDto> itemDtos = order.getItems().stream()
            .map(OrderResponse::toItemDto)
            .collect(Collectors.toList());

    return OrderResponse.builder()
            .id(order.getId())
            .customer(customerDto)
            .status(order.getStatus())
            .totalAmount(order.getTotalAmount())
            .shippingAddress(shippingAddressDto)
            .items(itemDtos)
            .createdAt(order.getCreatedAt())
            .updatedAt(order.getUpdatedAt())
            .notes(order.getNotes())
            .build();
  }

  private static String formatAddress(Address address) {
    StringBuilder sb = new StringBuilder();
    sb.append(address.getStreet());
    if (address.getCity() != null && !address.getCity().isEmpty()) {
      sb.append(", ").append(address.getCity());
    }
    if (address.getState() != null && !address.getState().isEmpty()) {
      sb.append(", ").append(address.getState());
    }
    if (address.getZipCode() != null && !address.getZipCode().isEmpty()) {
      sb.append(" ").append(address.getZipCode());
    }
    sb.append(", ").append(address.getCountry());
    return sb.toString();
  }

  private static OrderItemDto toItemDto(OrderItem item) {
    return OrderItemDto.builder()
            .productId(item.getProductId())
            .productName(item.getProductName())
            .price(item.getPrice())
            .quantity(item.getQuantity())
            .subtotal(item.getSubtotal())
            .build();
  }
}