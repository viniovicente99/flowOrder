package com.store.order_service.controller;

import com.store.order_service.model.Order;
import com.store.order_service.repository.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderRepository orderRepository;

    @GetMapping
    public ResponseEntity<List<Order>> getAll() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findOrderById(@PathVariable UUID id) {
        return orderRepository.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Order> postOrder(@Valid @RequestBody Order order) {

        order.setId(null);
        order.setCreatedAt(null);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderRepository.save(order));
    }

}
