package com.ym.orderservice.infrastructure.persistence.repository;

import com.ym.orderservice.domain.model.aggregate.Order;
import com.ym.orderservice.domain.model.entity.OrderItem;
import com.ym.orderservice.domain.model.valueobject.Address;
import com.ym.orderservice.domain.model.valueobject.Customer;
import com.ym.orderservice.domain.repository.OrderRepository;
import com.ym.orderservice.infrastructure.persistence.entity.*;
import com.ym.orderservice.infrastructure.persistence.mapper.OrderMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class OrderRepositoryImpl implements OrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final CustomerJpaRepository customerJpaRepository;
    private final OrderMapper orderMapper;

    @Override
    public Order save(Order order) {
        log.debug("Saving order - ID should be null: {}", order.getId());

        try {
            if (order.getId() != null) {
                log.warn("Order has ID {} but should be null for new order", order.getId());
            }

            return createNewOrder(order);
        } catch (Exception e) {
            log.error("Error saving order: {}", e.getMessage(), e);
            throw e;
        }
    }

    private Order createNewOrder(Order order) {
        log.info("Creating new order");

        CustomerEntity customerEntity = findOrCreateCustomer(order.getCustomer());

        AddressEntity shippingAddressEntity = new AddressEntity();
        shippingAddressEntity.setStreet(order.getShippingAddress().getStreet());
        shippingAddressEntity.setCity(order.getShippingAddress().getCity());
        shippingAddressEntity.setState(order.getShippingAddress().getState());
        shippingAddressEntity.setZipCode(order.getShippingAddress().getZipCode());
        shippingAddressEntity.setCountry(order.getShippingAddress().getCountry());
        shippingAddressEntity.setAddressType(AddressType.SHIPPING);
        shippingAddressEntity.setCustomer(customerEntity);

        entityManager.persist(shippingAddressEntity);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCustomer(customerEntity);
        orderEntity.setStatus(order.getStatus());
        orderEntity.setTotalAmount(order.getTotalAmount());
        orderEntity.setShippingAddress(shippingAddressEntity);
        orderEntity.setCreatedAt(order.getCreatedAt());
        orderEntity.setUpdatedAt(order.getUpdatedAt());
        orderEntity.setNotes(order.getNotes());

        entityManager.persist(orderEntity);
        log.info("Order persisted with generated ID: {}", orderEntity.getId());

        shippingAddressEntity.setOrder(orderEntity);
        entityManager.merge(shippingAddressEntity);

        for (OrderItem item : order.getItems()) {
            OrderItemEntity itemEntity = new OrderItemEntity();
            itemEntity.setOrder(orderEntity);
            itemEntity.setProductId(item.getProductId());
            itemEntity.setProductName(item.getProductName());
            itemEntity.setPrice(item.getPrice());
            itemEntity.setQuantity(item.getQuantity());
            itemEntity.setSubtotal(item.getSubtotal());

            entityManager.persist(itemEntity);
        }

        entityManager.flush();

        log.info("New order created successfully with id: {}", orderEntity.getId());

        return orderMapper.toDomain(orderEntity);
    }

    private CustomerEntity findOrCreateCustomer(Customer customer) {
        if (customer.getId() != null) {
            return customerJpaRepository.findByCustomerId(customer.getId())
                    .map(existingCustomer -> {
                        existingCustomer.setFullName(customer.getFullName());
                        existingCustomer.setEmail(customer.getEmail());
                        existingCustomer.setPhone(customer.getPhone());
                        return entityManager.merge(existingCustomer);
                    })
                    .orElseThrow(() -> new RuntimeException(
                            "Customer with ID " + customer.getId() + " not found"));
        }

        return customerJpaRepository.findByEmail(customer.getEmail())
                .map(existingCustomer -> {
                    existingCustomer.setFullName(customer.getFullName());
                    existingCustomer.setPhone(customer.getPhone());
                    return entityManager.merge(existingCustomer);
                })
                .orElseGet(() -> {
                    CustomerEntity newCustomer = new CustomerEntity();
                    newCustomer.setFullName(customer.getFullName());
                    newCustomer.setEmail(customer.getEmail());
                    newCustomer.setPhone(customer.getPhone());
                    entityManager.persist(newCustomer);
                    log.info("Created new customer with ID: {}", newCustomer.getCustomerId());
                    return newCustomer;
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findById(UUID id) {
        OrderEntity orderEntity = entityManager.find(OrderEntity.class, id);
        return Optional.ofNullable(orderEntity).map(orderMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return entityManager.createQuery(
                        "SELECT DISTINCT o FROM OrderEntity o LEFT JOIN FETCH o.items",
                        OrderEntity.class)
                .getResultList()
                .stream()
                .map(orderMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByCustomerId(Long customerId) {
        return entityManager.createQuery(
                        "SELECT o FROM OrderEntity o WHERE o.customer.customerId = :customerId",
                        OrderEntity.class)
                .setParameter("customerId", customerId)
                .getResultList()
                .stream()
                .map(orderMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByStatus(String status) {
        return entityManager.createQuery(
                        "SELECT o FROM OrderEntity o WHERE o.status = :status",
                        OrderEntity.class)
                .setParameter("status", com.ym.orderservice.domain.model.valueobject.OrderStatus.valueOf(status))
                .getResultList()
                .stream()
                .map(orderMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        OrderEntity orderEntity = entityManager.find(OrderEntity.class, id);
        if (orderEntity != null) {
            entityManager.remove(orderEntity);
            log.info("Order deleted successfully with id: {}", id);
        }
    }

    @Override
    public boolean existsById(UUID id) {
        return entityManager.find(OrderEntity.class, id) != null;
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(o) FROM OrderEntity o", Long.class)
                .getSingleResult();
    }

    @Override
    public long countByCustomerId(Long customerId) {
        return entityManager.createQuery(
                        "SELECT COUNT(o) FROM OrderEntity o WHERE o.customer.customerId = :customerId",
                        Long.class)
                .setParameter("customerId", customerId)
                .getSingleResult();
    }
}