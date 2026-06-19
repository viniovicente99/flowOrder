package com.store.order_service.service;

import com.store.order_service.model.Order;
import com.store.order_service.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    UUID id = UUID.randomUUID();

    public Order generateOrder() {
        return new Order(
                id, "Test Product", new BigDecimal("99.99"), "CREATED",
                "CREATING_ORDER"
        );
    }

    @Test
    @DisplayName("Should return a order list with only one item.")
    public void shouldReturnAllOrders() {

        Order order = generateOrder();

        Mockito.when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));

        List<Order> orderList = orderService.getAll();

        Assertions.assertEquals(1, orderList.size());
    }

    @Test
    @DisplayName("Should set to null the id sent by the client when creating a order")
    public void shouldReturnNull() {

        Order order = generateOrder();

        Mockito.when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.createOrder(order);

        Assertions.assertNull(result.getId());

    }

    @Test
    @DisplayName("Should create a Order successfully.")
    public void shouldCreateOrder() {

        Order order = generateOrder();

        Mockito.when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.createOrder(order);

        Assertions.assertNotNull(result);

    }




}