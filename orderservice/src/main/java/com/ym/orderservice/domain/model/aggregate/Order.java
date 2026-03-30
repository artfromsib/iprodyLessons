package com.ym.orderservice.domain.model.aggregate;

import com.ym.orderservice.domain.model.entity.OrderItem;
import com.ym.orderservice.domain.model.valueobject.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
public class Order {
    private UUID id;
    private final Customer customer;
    private final List<OrderItem> items;
    private final Address shippingAddress;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String notes;

    public Order(Customer customer, List<OrderItem> items, Address shippingAddress) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }
        if (shippingAddress == null) {
            throw new IllegalArgumentException("Shipping address cannot be null");
        }

        this.id = null;
        this.customer = customer;
        this.items = new ArrayList<>(items);
        this.shippingAddress = shippingAddress;
        this.status = OrderStatus.CREATED;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.notes = "";
        calculateTotalAmount();
    }

    public Order(UUID id,
                 Customer customer,
                 List<OrderItem> items,
                 Address shippingAddress,
                 OrderStatus status,
                 BigDecimal totalAmount,
                 LocalDateTime createdAt,
                 LocalDateTime updatedAt,
                 String notes) {
        this.id = id;
        this.customer = customer;
        this.items = new ArrayList<>(items);
        this.shippingAddress = shippingAddress;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.notes = notes != null ? notes : "";
    }

    private void calculateTotalAmount() {
        this.totalAmount = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void updateStatus(OrderStatus newStatus) {
        if (this.status == OrderStatus.DELIVERED || this.status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot update status of " + this.status + " order");
        }
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public void addNotes(String notes) {
        if (notes != null && !notes.trim().isEmpty()) {
            this.notes = notes;
            this.updatedAt = LocalDateTime.now();
        }
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}