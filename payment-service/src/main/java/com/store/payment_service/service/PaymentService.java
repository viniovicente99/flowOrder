package com.store.payment_service.service;

import com.store.payment_service.model.Order;
import com.store.payment_service.model.Payment;
import com.store.payment_service.repository.PaymentRepository;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {

    @Value("${rabbitmq.orderqueue}")
    private String orderQueue;

    @Value("${rabbitmq.paymentqueue}")
    private String paymentQueue;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener( containerFactory = "listenerContainerFactory", queues = "${rabbitmq.orderqueue}")
    public void receiveMessage (Order order) {

        System.out.println("New RMQ message received.");

        Payment payment = processOrder(order);

        paymentRepository.save(payment);

        System.out.println("Payment processed and published to queue.");
        publishPayment(payment, paymentQueue);

    }

    public Payment processOrder(Order order) {

        Payment payment = new Payment();

        payment.setOrderId(order.getId());
        payment.setAmount(order.getAmount());

        if (order.getAmount().compareTo(new BigDecimal(5000)) > 0) {
            payment.setStatus("DECLINED");
            payment.setFailureReason("LIMIT_EXCEEDED");
        } else {
            payment.setStatus("APPROVED");
        }

        return payment;
    }

    public void publishPayment(Payment payment, String queueName) {
        try {
            rabbitTemplate.convertAndSend(queueName, payment);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
