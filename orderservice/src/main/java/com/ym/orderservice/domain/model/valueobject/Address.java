package com.ym.orderservice.domain.model.valueobject;

import lombok.Value;

@Value
public class Address {
    String street;
    String city;
    String state;
    String zipCode;
    String country;

    protected Address() {
        this.street = "";
        this.city = "";
        this.state = "";
        this.zipCode = "";
        this.country = "";
    }

    public Address(String street, String city, String state, String zipCode, String country) {
        if (street == null || street.trim().isEmpty()) {
            throw new IllegalArgumentException("Street cannot be null or empty");
        }
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be null or empty");
        }
        if (country == null || country.trim().isEmpty()) {
            throw new IllegalArgumentException("Country cannot be null or empty");
        }
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }
}