package com.ym.orderservice.infrastructure.persistence.repository;

import com.ym.orderservice.infrastructure.persistence.entity.AddressEntity;
import com.ym.orderservice.infrastructure.persistence.entity.AddressType;
import com.ym.orderservice.infrastructure.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AddressJpaRepository extends JpaRepository<AddressEntity, UUID> {
  List<AddressEntity> findByCustomer(CustomerEntity customer);
  List<AddressEntity> findByCustomerAndAddressType(CustomerEntity customer, AddressType addressType);
}