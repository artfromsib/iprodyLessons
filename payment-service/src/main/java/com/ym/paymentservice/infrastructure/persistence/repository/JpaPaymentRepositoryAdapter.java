package com.ym.paymentservice.infrastructure.persistence.repository;


import com.ym.paymentservice.domain.model.Payment;
import com.ym.paymentservice.domain.model.PaymentId;
import com.ym.paymentservice.domain.repository.PaymentRepository;
import com.ym.paymentservice.infrastructure.persistence.entity.PaymentEntity;
import com.ym.paymentservice.infrastructure.persistence.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaPaymentRepositoryAdapter implements PaymentRepository {

    private final JpaPaymentRepository jpaPaymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public Payment save(Payment payment) {
        PaymentEntity entity = paymentMapper.toEntity(payment);
        PaymentEntity savedEntity = jpaPaymentRepository.save(entity);
        return paymentMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Payment> findById(PaymentId id) {
        return jpaPaymentRepository.findById(id.getValue())
                .map(paymentMapper::toDomain);
    }

    @Override
    public List<Payment> findAll() {
        return jpaPaymentRepository.findAll().stream()
                .map(paymentMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(PaymentId id) {
        jpaPaymentRepository.deleteById(id.getValue());
    }

    @Override
    public boolean existsById(PaymentId id) {
        return jpaPaymentRepository.existsById(id.getValue());
    }
}