
package com.ym.orderservice.infrastructure.persistence.repository;

import com.ym.orderservice.infrastructure.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {
  Optional<CustomerEntity> findByCustomerId(Long customerId);
  Optional<CustomerEntity> findByEmail(String email);
  boolean existsByCustomerId(Long customerId);
}