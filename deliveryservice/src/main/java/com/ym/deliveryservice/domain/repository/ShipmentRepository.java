package com.ym.deliveryservice.domain.repository;

import com.ym.deliveryservice.domain.model.DeliveryStatus;
import com.ym.deliveryservice.domain.model.Shipment;
import com.ym.deliveryservice.domain.model.ShipmentId;
import com.ym.deliveryservice.domain.model.TrackingNumber;

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
}