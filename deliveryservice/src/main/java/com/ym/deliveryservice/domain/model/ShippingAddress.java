package com.ym.deliveryservice.domain.model;

import lombok.Value;

@Value
public class ShippingAddress {
  String street;
  String city;
  String state;
  String zipCode;
  String country;

  public ShippingAddress(String street, String city, String state, String zipCode, String country) {
    if (street == null || street.trim().isEmpty()) {
      throw new IllegalArgumentException("Street cannot be empty");
    }
    if (city == null || city.trim().isEmpty()) {
      throw new IllegalArgumentException("City cannot be empty");
    }
    if (zipCode == null || zipCode.trim().isEmpty()) {
      throw new IllegalArgumentException("Zip code cannot be empty");
    }
    if (country == null || country.trim().isEmpty()) {
      throw new IllegalArgumentException("Country cannot be empty");
    }

    this.street = street;
    this.city = city;
    this.state = state;
    this.zipCode = zipCode;
    this.country = country;
  }

  public String getFullAddress() {
    StringBuilder sb = new StringBuilder();
    sb.append(street);
    if (state != null && !state.isEmpty()) {
      sb.append(", ").append(city).append(", ").append(state);
    } else {
      sb.append(", ").append(city);
    }
    sb.append(" ").append(zipCode).append(", ").append(country);
    return sb.toString();
  }
}
