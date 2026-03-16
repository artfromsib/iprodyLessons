package com.ym.orderservice.infrastructure.web.dto;

import com.ym.orderservice.domain.model.aggregate.Order;
import com.ym.orderservice.domain.model.entity.OrderItem;
import com.ym.orderservice.domain.model.valueobject.Address;
import com.ym.orderservice.domain.model.valueobject.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
public class OrderResponse {
  private UUID id;
  private CustomerDto customer;
  private OrderStatus status;
  private BigDecimal totalAmount;
  private AddressDto shippingAddress;
  private List<OrderItemDto> items;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String notes;

  @Data
  @Builder
  public static class CustomerDto {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String formattedPhone;
    private AddressDto address;
  }

  @Data
  @Builder
  public static class AddressDto {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String formattedAddress;
  }

  @Data
  @Builder
  public static class OrderItemDto {
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
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