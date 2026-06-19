package com.store.order_service.controller;

import com.store.order_service.model.Order;
import com.store.order_service.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Value("${rabbitmq.queuename}")
    private String queueName;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable UUID id) {
        return orderService.getById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Order> postOrder(@Valid @RequestBody Order order) {

        Order savedOrder = orderService.createOrder(order);

        orderService.publishOrder(order, queueName);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

}
