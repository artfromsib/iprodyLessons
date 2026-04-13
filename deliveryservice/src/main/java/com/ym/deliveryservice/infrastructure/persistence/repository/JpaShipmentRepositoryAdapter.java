package com.ym.deliveryservice.infrastructure.persistence.repository;


import com.ym.deliveryservice.domain.model.*;
import com.ym.deliveryservice.domain.repository.ShipmentRepository;
import com.ym.deliveryservice.infrastructure.persistence.entity.ShipmentEntity;
import com.ym.deliveryservice.infrastructure.persistence.mapper.ShipmentMapper;
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

    @Override
    public Optional<Shipment> findByOrderId(OrderId orderId) {
        return jpaShipmentRepository.findByOrderId(orderId.getValue())
                .map(shipmentMapper::toDomain);
    }
}