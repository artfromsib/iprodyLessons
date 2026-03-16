package com.ym.orderservice.infrastructure.persistence.repository;

import com.ym.orderservice.infrastructure.persistence.entity.OrderEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface JpaOrderRepository extends JpaRepository<OrderEntity, UUID> {

  List<OrderEntity> findByStatus(String status);

  List<OrderEntity> findByCustomerCustomerId(Long customerId);

  List<OrderEntity> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

  @Query("SELECT o FROM OrderEntity o ORDER BY o.createdAt DESC")
  List<OrderEntity> findTopNOrderByCreatedAtDesc(Pageable pageable);
  default List<OrderEntity> findTopNOrderByCreatedAtDesc(int limit) {
    return findTopNOrderByCreatedAtDesc(Pageable.ofSize(limit));
  }

  @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.customer.customerId = :customerId")
  long countByCustomerId(@Param("customerId") Long customerId);

  @Query("SELECT SUM(o.totalAmount) FROM OrderEntity o WHERE o.customer.customerId = :customerId")
  Double getTotalSpentByCustomer(@Param("customerId") Long customerId);

  boolean existsByCustomerCustomerIdAndStatus(Long customerId, String status);
}