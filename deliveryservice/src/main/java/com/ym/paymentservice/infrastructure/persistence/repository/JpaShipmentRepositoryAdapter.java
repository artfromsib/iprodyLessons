package com.ym.paymentservice.infrastructure.persistence.repository;


import com.ym.paymentservice.domain.model.DeliveryStatus;
import com.ym.paymentservice.domain.model.Shipment;
import com.ym.paymentservice.domain.model.ShipmentId;
import com.ym.paymentservice.domain.model.TrackingNumber;
import com.ym.paymentservice.domain.repository.ShipmentRepository;
import com.ym.paymentservice.infrastructure.persistence.entity.ShipmentEntity;
import com.ym.paymentservice.infrastructure.persistence.mapper.ShipmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaShipmentRepositoryAdapter implements ShipmentRepository {

  private final JpaShipmentRepository jpaShipmentRepository;
  private final ShipmentMapper shipmentMapper;

  @Override
  public Shipment save(Shipment shipment) {
    ShipmentEntity entity = shipmentMapper.toEntity(shipment);
    ShipmentEntity savedEntity = jpaShipmentRepository.save(entity);
    return shipmentMapper.toDomain(savedEntity);
  }

  @Override
  public Optional<Shipment> findById(ShipmentId id) {
    return jpaShipmentRepository.findById(id.getValue())
            .map(shipmentMapper::toDomain);
  }

  @Override
  public Optional<Shipment> findByTrackingNumber(TrackingNumber trackingNumber) {
    return jpaShipmentRepository.findByTrackingNumber(trackingNumber.getValue())
            .map(shipmentMapper::toDomain);
  }

  @Override
  public List<Shipment> findAll() {
    return jpaShipmentRepository.findAll().stream()
            .map(shipmentMapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public List<Shipment> findByStatus(DeliveryStatus status) {
    return jpaShipmentRepository.findByStatus(
                    ShipmentEntity.DeliveryStatus.valueOf(status.name()))
            .stream()
            .map(shipmentMapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public void deleteById(ShipmentId id) {
    jpaShipmentRepository.deleteById(id.getValue());
  }

  @Override
  public boolean existsById(ShipmentId id) {
    return jpaShipmentRepository.existsById(id.getValue());
  }
}