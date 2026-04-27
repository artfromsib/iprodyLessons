package com.ym.paymentservice.infrastructure.persistence.repository;

import com.ym.paymentservice.domain.model.OrderId;
import com.ym.paymentservice.domain.model.Payment;
import com.ym.paymentservice.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaPaymentRepository extends JpaRepository<PaymentEntity, String> {
    void deleteByOrderId(OrderId orderId);
}