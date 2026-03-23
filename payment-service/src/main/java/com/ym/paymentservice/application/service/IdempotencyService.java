package com.ym.paymentservice.application.service;

import com.ym.paymentservice.infrastructure.persistence.entity.IdempotencyKey;

import java.util.Optional;

public interface IdempotencyService {
  void createPendingKey(String key);
  Optional<IdempotencyKey>  getByKey(String key);
  void markKeyAsCompleted(String key, String responseData, int statusCode);
}
