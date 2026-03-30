package com.ym.paymentservice.application.service;

import com.ym.paymentservice.domain.model.*;
import com.ym.paymentservice.domain.model.enums.PaymentStatus;
import com.ym.paymentservice.domain.repository.PaymentRepository;
import com.ym.paymentservice.interfaces.dto.PaymentRequestDTO;
import com.ym.paymentservice.interfaces.dto.PaymentResponseDTO;
import com.ym.paymentservice.interfaces.dto.PaymentUpdateDTO;
import com.ym.paymentservice.interfaces.exception.PaymentNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentResponseDTO payProcess(PaymentRequestDTO request) {
        try {
            return createPayment(request, PaymentStatus.SUCCESS);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public PaymentResponseDTO createPayment(PaymentRequestDTO request, PaymentStatus status) {
        PaymentId paymentId = new PaymentId(UUID.randomUUID().toString());
        OrderId orderId = new OrderId(request.getOrderId());
        CustomerId customerId = new CustomerId(request.getCustomerId());

        Cost cost = new Cost(request.getAmount(), request.getCurrency());


        PaymentMethod.PaymentMethodType methodType =
                request.getPaymentMethodType() != null && !request.getPaymentMethodType().trim().isEmpty()
                        ? PaymentMethod.PaymentMethodType.valueOf(request.getPaymentMethodType())
                        : null;

        String providerToken =
                request.getProviderToken() != null && !request.getProviderToken().trim().isEmpty()
                        ? request.getProviderToken()
                        : null;
        PaymentMethod paymentMethod = methodType != null && providerToken != null ? new PaymentMethod(methodType, providerToken) : null;
        Payment payment = Payment.builder()
                .id(paymentId)
                .orderId(orderId)
                .customerId(customerId)
                .cost(cost)
                .status(status)
                .paymentMethod(paymentMethod)
                .createdAt(LocalDateTime.now())
                .build();

        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment created with ID: {}", savedPayment.getId().getValue());

        return PaymentResponseDTO.fromDomain(savedPayment);
    }

    @Transactional(readOnly = true)
    public PaymentResponseDTO getPayment(String id) {
        Payment payment = findPaymentById(id);
        return PaymentResponseDTO.fromDomain(payment);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(PaymentResponseDTO::fromDomain)
                .collect(Collectors.toList());
    }

    @Transactional
    public PaymentResponseDTO updatePayment(String id, PaymentUpdateDTO updateDTO) {
        Payment payment = findPaymentById(id);

        if (updateDTO.getStatus() != null) {
            switch (updateDTO.getStatus()) {
                case "PROCESSING":
                    payment.process();
                    break;
                case "SUCCESS":
                    payment.complete(
                            updateDTO.getProviderTransactionId(),
                            updateDTO.getProviderName(),
                            updateDTO.getRawResponse()
                    );
                    break;
                case "FAILED":
                    payment.fail(updateDTO.getFailureReason());
                    break;
                case "REFUNDED":
                    payment.refund();
                    break;
                default:
                    throw new IllegalArgumentException("Invalid status transition");
            }
        }

        Payment updatedPayment = paymentRepository.save(payment);
        log.info("Payment updated with ID: {}", updatedPayment.getId().getValue());

        return PaymentResponseDTO.fromDomain(updatedPayment);
    }

    @Transactional
    public void deletePayment(String id) {
        PaymentId paymentId = new PaymentId(id);
        if (!paymentRepository.existsById(paymentId)) {
            throw new PaymentNotFoundException("Payment not found with id: " + id);
        }
        paymentRepository.deleteById(paymentId);
        log.info("Payment deleted with ID: {}", id);
    }

    private Payment findPaymentById(String id) {
        PaymentId paymentId = new PaymentId(id);
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + id));
    }
}