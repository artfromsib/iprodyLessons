package com.ym.paymentservice.domain.repository;

import com.ym.paymentservice.domain.model.OrderId;
import com.ym.paymentservice.domain.model.Payment;
import com.ym.paymentservice.domain.model.PaymentId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {
    Payment save(Payment payment);

    Optional<Payment> findById(PaymentId id);

    List<Payment> findAll();

    void deleteById(PaymentId id);

    boolean existsById(PaymentId id);

    void deleteByOrderId(OrderId orderId);
}