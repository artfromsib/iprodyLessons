package com.ym.orderservice.application.service;

import com.ym.orderservice.application.exception.CustomerNotFoundException;
import com.ym.orderservice.application.exception.OrderNotFoundException;
import com.ym.orderservice.domain.model.aggregate.Order;
import com.ym.orderservice.domain.model.entity.OrderItem;
import com.ym.orderservice.domain.model.valueobject.*;
import com.ym.orderservice.domain.repository.OrderRepository;
import com.ym.orderservice.infrastructure.persistence.entity.AddressType;
import com.ym.orderservice.infrastructure.persistence.repository.AddressJpaRepository;
import com.ym.orderservice.infrastructure.persistence.repository.CustomerJpaRepository;
import com.ym.orderservice.infrastructure.web.dto.OrderRequest;
import com.ym.orderservice.infrastructure.web.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerJpaRepository customerJpaRepository;
    private final AddressJpaRepository addressJpaRepository;

    public OrderResponse createOrder(OrderRequest request) {
        Address customerAddress = new Address(
                request.getCustomer().getAddress().getStreet(),
                request.getCustomer().getAddress().getCity(),
                request.getCustomer().getAddress().getState(),
                request.getCustomer().getAddress().getZipCode(),
                request.getCustomer().getAddress().getCountry()
        );

        Customer customer = new Customer(
                request.getCustomer().getId(),
                request.getCustomer().getFullName(),
                request.getCustomer().getEmail(),
                request.getCustomer().getPhone(),
                customerAddress
        );

        Address shippingAddress = new Address(
                request.getShippingAddress().getStreet(),
                request.getShippingAddress().getCity(),
                request.getShippingAddress().getState(),
                request.getShippingAddress().getZipCode(),
                request.getShippingAddress().getCountry()
        );

        List<OrderItem> items = request.getItems().stream()
                .map(itemRequest -> new OrderItem(
                        itemRequest.getProductId(),
                        itemRequest.getProductName(),
                        itemRequest.getPrice(),
                        itemRequest.getQuantity()
                ))
                .collect(Collectors.toList());

        Order order = new Order(customer, items, shippingAddress);

        if (request.getNotes() != null && !request.getNotes().isEmpty()) {
            order.addNotes(request.getNotes());
        }

        Order savedOrder = orderRepository.save(order);

        return OrderResponse.fromDomain(savedOrder);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(UUID id) {
        return orderRepository.findById(id)
                .map(OrderResponse::fromDomain)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::fromDomain)
                .collect(Collectors.toList());
    }

    public OrderResponse updateOrderStatus(UUID id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));

        order.updateStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);

        return OrderResponse.fromDomain(updatedOrder);
    }

    public OrderResponse addOrderNotes(UUID id, String notes) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));

        order.addNotes(notes);
        Order updatedOrder = orderRepository.save(order);

        return OrderResponse.fromDomain(updatedOrder);
    }


    public void deleteOrder(UUID id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByCustomer(Long customerId) {
        return customerJpaRepository.findByCustomerId(customerId)
                .map(customer -> customer.getOrders().stream()
                        .map(orderEntity -> orderRepository.findById(orderEntity.getId()))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(OrderResponse::fromDomain)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));
    }
}