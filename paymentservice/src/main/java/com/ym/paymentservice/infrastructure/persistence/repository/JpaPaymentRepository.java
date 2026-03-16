package com.ym.paymentservice.infrastructure.persistence.repository;

import com.ym.paymentservice.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPaymentRepository extends JpaRepository<PaymentEntity, String> {
}