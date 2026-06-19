package com.store.order_service.controller;

import com.store.order_service.model.Order;
import com.store.order_service.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Orders", description = "Order endpoints management")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Value("${rabbitmq.queuename}")
    private String queueName;

    @GetMapping
    @Operation(
            summary = "Returns all Orders.",
            description = "Returns all orders stored in the database in a list format."
    )
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Returns an Order by its ID if it exists.",
            description = "Returns an order. The ID must be a valid UUID."
    )
    public ResponseEntity<Order> getOrderById(@PathVariable UUID id) {
        return orderService.getById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    @Operation(
            summary = "Create Orders",
            description = "Creates an order. The ID and creation date are set to null so they are" +
                    " generated automatically by the database." +
                    "<br> </br>A RabbitMQ server must be running for this endpoint to work correctly."
    )
    public ResponseEntity<Order> postOrder(@Valid @RequestBody Order order) {

        Order savedOrder = orderService.createOrder(order);

        orderService.publishOrder(order, queueName);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

}
