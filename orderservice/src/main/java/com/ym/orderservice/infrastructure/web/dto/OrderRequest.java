package com.ym.orderservice.infrastructure.web.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Data
@Getter
public class OrderRequest {
  private CustomerDto customer;
  private AddressDto shippingAddress;
  private List<OrderItemRequest> items;
  private String notes;

  @Data
  public static class CustomerDto {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private AddressDto address;
  }

  @Data
  public static class AddressDto {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
  }
}