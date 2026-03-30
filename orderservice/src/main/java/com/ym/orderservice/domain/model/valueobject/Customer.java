package com.ym.orderservice.domain.model.valueobject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.UUID;

@Value
public class Customer {
    Long id;
    String fullName;
    String email;
    String phone;
    Address address;

    @JsonCreator
    public Customer(
            @JsonProperty("id") Long id,
            @JsonProperty("fullName") String fullName,
            @JsonProperty("email") String email,
            @JsonProperty("phone") String phone,
            @JsonProperty("address") Address address) {

        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty");
        }
        if (email == null || !isValidEmail(email)) {
            throw new IllegalArgumentException("Valid email is required");
        }
        if (phone == null || !isValidPhone(phone)) {
            throw new IllegalArgumentException("Valid phone number is required");
        }
        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }

        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }


    public boolean isNew() {
        return id == null;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("^[+]?[(]?[0-9]{1,3}[)]?[-\\s.]?[(]?[0-9]{1,4}[)]?[-\\s.]?[0-9]{3,4}[-\\s.]?[0-9]{3,4}$");
    }

    public String getFormattedPhone() {
        if (phone == null) return "";
        return phone.replaceAll("(\\d{3})(\\d{3})(\\d{4})", "($1) $2-$3");
    }
}