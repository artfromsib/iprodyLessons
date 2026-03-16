package com.ym.paymentservice.domain.repository;

import com.ym.paymentservice.domain.model.DeliveryStatus;
import com.ym.paymentservice.domain.model.Shipment;
import com.ym.paymentservice.domain.model.ShipmentId;
import com.ym.paymentservice.domain.model.TrackingNumber;

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