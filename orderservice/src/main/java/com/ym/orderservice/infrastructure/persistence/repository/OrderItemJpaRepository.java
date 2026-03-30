package com.ym.orderservice.infrastructure.persistence.repository;

import com.ym.orderservice.infrastructure.persistence.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderItemJpaRepository extends JpaRepository<OrderItemEntity, UUID> {
    void deleteByOrderId(UUID orderId);
}