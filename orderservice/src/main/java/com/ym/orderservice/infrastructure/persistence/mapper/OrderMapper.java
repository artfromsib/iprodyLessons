package com.ym.orderservice.infrastructure.persistence.mapper;

import com.ym.orderservice.domain.model.aggregate.Order;
import com.ym.orderservice.domain.model.entity.OrderItem;
import com.ym.orderservice.domain.model.valueobject.Address;
import com.ym.orderservice.domain.model.valueobject.Customer;
import com.ym.orderservice.infrastructure.persistence.entity.*;
import com.ym.orderservice.infrastructure.persistence.repository.AddressJpaRepository;
import com.ym.orderservice.infrastructure.persistence.repository.CustomerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final CustomerJpaRepository customerJpaRepository;
    private final AddressJpaRepository addressJpaRepository;

    public OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());

        CustomerEntity customerEntity = findOrCreateCustomer(order.getCustomer());
        entity.setCustomer(customerEntity);

        AddressEntity shippingAddressEntity = createAddressEntity(
                order.getShippingAddress(),
                AddressType.SHIPPING,
                customerEntity
        );
        entity.setShippingAddress(shippingAddressEntity);

        entity.setStatus(order.getStatus());
        entity.setTotalAmount(order.getTotalAmount());
        entity.setCreatedAt(order.getCreatedAt());
        entity.setUpdatedAt(order.getUpdatedAt());
        entity.setNotes(order.getNotes());

        var items = order.getItems().stream()
                .map(item -> toItemEntity(item, entity))
                .collect(Collectors.toList());
        entity.setItems(items);

        return entity;
    }

    private CustomerEntity findOrCreateCustomer(Customer customer) {
        return customerJpaRepository.findByCustomerId(customer.getId())
                .orElseGet(() -> {
                    CustomerEntity newCustomer = new CustomerEntity();
                    newCustomer.setCustomerId(customer.getId());
                    newCustomer.setFullName(customer.getFullName());
                    newCustomer.setEmail(customer.getEmail());
                    newCustomer.setPhone(customer.getPhone());
                    return customerJpaRepository.save(newCustomer);
                });
    }

    private AddressEntity createAddressEntity(Address address, AddressType type, CustomerEntity customer) {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setStreet(address.getStreet());
        addressEntity.setCity(address.getCity());
        addressEntity.setState(address.getState());
        addressEntity.setZipCode(address.getZipCode());
        addressEntity.setCountry(address.getCountry());
        addressEntity.setAddressType(type);
        addressEntity.setCustomer(customer);
        return addressEntity;
    }

    private OrderItemEntity toItemEntity(OrderItem item, OrderEntity order) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.setOrder(order);
        entity.setProductId(item.getProductId());
        entity.setProductName(item.getProductName());
        entity.setPrice(item.getPrice());
        entity.setQuantity(item.getQuantity());
        entity.setSubtotal(item.getSubtotal());
        return entity;
    }

    public Order toDomain(OrderEntity entity) {
        Address customerAddress = getCustomerAddress(entity.getCustomer());

        Customer customer = new Customer(
                entity.getCustomer().getCustomerId(),
                entity.getCustomer().getFullName(),
                entity.getCustomer().getEmail(),
                entity.getCustomer().getPhone(),
                customerAddress
        );

        Address shippingAddress = new Address(
                entity.getShippingAddress().getStreet(),
                entity.getShippingAddress().getCity(),
                entity.getShippingAddress().getState(),
                entity.getShippingAddress().getZipCode(),
                entity.getShippingAddress().getCountry()
        );

        var items = entity.getItems().stream()
                .map(this::toDomainItem)
                .collect(Collectors.toList());

        return new Order(
                entity.getId(),
                customer,
                items,
                shippingAddress,
                entity.getStatus(),
                entity.getTotalAmount(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getNotes()
        );
    }

    private Address getCustomerAddress(CustomerEntity customerEntity) {
        return addressJpaRepository.findByCustomerAndAddressType(customerEntity, AddressType.HOME)
                .stream()
                .findFirst()
                .map(addr -> new Address(
                        addr.getStreet(),
                        addr.getCity(),
                        addr.getState(),
                        addr.getZipCode(),
                        addr.getCountry()
                ))
                .orElseGet(() -> new Address(
                        "Unknown",
                        "Unknown",
                        "",
                        "",
                        "Unknown"
                ));
    }

    private OrderItem toDomainItem(OrderItemEntity entity) {
        return new OrderItem(
                entity.getProductId(),
                entity.getProductName(),
                entity.getPrice(),
                entity.getQuantity()
        );
    }
}