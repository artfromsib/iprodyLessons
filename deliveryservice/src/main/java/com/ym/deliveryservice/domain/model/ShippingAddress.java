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
