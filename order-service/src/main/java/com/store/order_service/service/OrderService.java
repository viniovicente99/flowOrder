package com.store.order_service.service;

import com.store.order_service.model.Order;
import com.store.order_service.repository.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public Optional<Order> getById(UUID id) {
        return orderRepository.findById(id);
    }

    public Order createOrder(Order order) {

        order.setId(null);
        order.setCreatedAt(null);

        return orderRepository.save(order);

    }

    public void publishOrder(Order order, String queueName) {
        try {
            System.out.println("Order published to queue.");
            rabbitTemplate.convertAndSend(queueName, order);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
