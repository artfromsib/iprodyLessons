package com.ym.deliveryservice.domain.repository;

import com.ym.deliveryservice.domain.model.*;

import java.util.List;
import java.util.Optional;

public interface ShipmentRepository {
    Shipment save(Shipment shipment);

    Optional<Shipment> findById(ShipmentId id);

    Optional<Shipment> findByTrackingNumber(TrackingNumber trackingNumber);

    List<Shipment> findAll();

    List<Shipment> findByStatus(DeliveryStatus status);

    void deleteById(ShipmentId id);

    boolean existsById(ShipmentId id);
    Optional<Shipment> findByOrderId(OrderId orderId);
}