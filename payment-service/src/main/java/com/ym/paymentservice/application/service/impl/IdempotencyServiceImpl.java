package com.ym.paymentservice.application.service.impl;

import com.ym.paymentservice.application.service.IdempotencyService;
import com.ym.paymentservice.domain.model.enums.KeyStatus;
import com.ym.paymentservice.domain.repository.IdempotencyRepository;
import com.ym.paymentservice.infrastructure.persistence.entity.IdempotencyKey;
import com.ym.paymentservice.interfaces.exception.IdempotencyKeyExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IdempotencyServiceImpl implements IdempotencyService {

  private final IdempotencyRepository repository;

  @Override
  public Optional<IdempotencyKey> getByKey(String key) {
    return repository.findById(key);
  }

  @Override
  public void markKeyAsCompleted(String key, String responseData, int statusCode) {
    var keyEntity = getByKey(key).orElseThrow(() -> new EntityNotFoundException("Key not found"));
    keyEntity.setStatus(KeyStatus.COMPLETED);
    keyEntity.setResponse(responseData);
    keyEntity.setStatusCode(statusCode);
    repository.save(keyEntity);
  }
  @Transactional
  @Override
  public void createPendingKey(String key) {
    var newKey = new IdempotencyKey(key, KeyStatus.PENDING);
    try {
      repository.save(newKey);
    } catch (DataIntegrityViolationException e) {
      throw new IdempotencyKeyExistsException("Key already exists", e);
    }
  }
}
