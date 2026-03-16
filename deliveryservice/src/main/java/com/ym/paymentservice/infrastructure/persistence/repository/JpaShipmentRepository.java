package com.ym.paymentservice.infrastructure.persistence.repository;

import com.ym.paymentservice.infrastructure.persistence.entity.ShipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaShipmentRepository extends JpaRepository<ShipmentEntity, String> {
  Optional<ShipmentEntity> findByTrackingNumber(String trackingNumber);
  List<ShipmentEntity> findByStatus(ShipmentEntity.DeliveryStatus status);
  boolean existsByOrderId(String orderId);
  boolean existsByTrackingNumber(String trackingNumber);
}