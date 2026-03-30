package com.ym.paymentservice.infrastructure.persistence.mapper;


import com.ym.paymentservice.domain.model.*;
import com.ym.paymentservice.domain.model.enums.PaymentStatus;
import com.ym.paymentservice.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.stereotype.Component;

import java.util.Currency;

@Component
public class PaymentMapper {

    public PaymentEntity toEntity(Payment payment) {
        if (payment == null) return null;

        PaymentEntity.PaymentMethodType methodType = null;
        if (payment.getPaymentMethod() != null && payment.getPaymentMethod().getType() != null) {
            methodType = PaymentEntity.PaymentMethodType.valueOf(payment.getPaymentMethod().getType().name());
        }

        PaymentEntity.PaymentStatus status = null;
        if (payment.getStatus() != null) {
            status = PaymentEntity.PaymentStatus.valueOf(payment.getStatus().name());
        }

        PaymentEntity.PaymentEntityBuilder builder = PaymentEntity.builder()
                .id(payment.getId() != null ? payment.getId().getValue() : null)
                .orderId(payment.getOrderId() != null ? payment.getOrderId().getValue() : null)
                .customerId(payment.getCustomerId() != null ? payment.getCustomerId().getValue() : null)
                .status(status)
                .paymentMethodType(methodType)
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt());

        if (payment.getCost() != null) {
            builder.amount(payment.getCost().getAmount())
                    .currency(payment.getCost().getCurrency().getCurrencyCode());
        }

        if (payment.getPaymentMethod() != null) {
            builder.providerToken(payment.getPaymentMethod().getProviderToken());
        }

        if (payment.getTransactionDetails() != null) {
            builder.providerTransactionId(payment.getTransactionDetails().getProviderTransactionId())
                    .providerName(payment.getTransactionDetails().getProviderName())
                    .rawResponse(payment.getTransactionDetails().getRawResponse());
        }

        return builder.build();
    }

    public Payment toDomain(PaymentEntity entity) {
        if (entity == null) return null;

        PaymentId paymentId = entity.getId() != null ? new PaymentId(entity.getId()) : null;
        OrderId orderId = entity.getOrderId() != null ? new OrderId(entity.getOrderId()) : null;
        CustomerId customerId = entity.getCustomerId() != null ? new CustomerId(entity.getCustomerId()) : null;

        Cost cost = null;
        if (entity.getAmount() != null && entity.getCurrency() != null) {
            cost = new Cost(entity.getAmount(), Currency.getInstance(entity.getCurrency()));
        }

        PaymentStatus status = null;
        if (entity.getStatus() != null) {
            status = PaymentStatus.valueOf(entity.getStatus().name());
        }

        PaymentMethod paymentMethod = null;
        if (entity.getPaymentMethodType() != null && entity.getProviderToken() != null) {
            PaymentMethod.PaymentMethodType methodType =
                    PaymentMethod.PaymentMethodType.valueOf(entity.getPaymentMethodType().name());
            paymentMethod = new PaymentMethod(methodType, entity.getProviderToken());
        }

        TransactionDetails transactionDetails = null;
        if (entity.getProviderTransactionId() != null && entity.getProviderName() != null) {
            transactionDetails = new TransactionDetails(
                    entity.getProviderTransactionId(),
                    entity.getProviderName(),
                    entity.getRawResponse()
            );
        }

        return Payment.builder()
                .id(paymentId)
                .orderId(orderId)
                .customerId(customerId)
                .cost(cost)
                .status(status)
                .paymentMethod(paymentMethod)
                .transactionDetails(transactionDetails)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}