package com.ym.orderservice.domain.repository;

import com.ym.orderservice.domain.model.aggregate.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findById(UUID id);

    List<Order> findAll();

    List<Order> findByCustomerId(Long customerId);

    List<Order> findByStatus(String status);

    void deleteById(UUID id);

    boolean existsById(UUID id);

    long count();

    long countByCustomerId(Long customerId);
}