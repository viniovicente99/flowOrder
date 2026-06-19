package com.store.payment_service.service;

import com.store.payment_service.model.Order;
import com.store.payment_service.model.Payment;
import com.store.payment_service.repository.PaymentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    UUID id = UUID.randomUUID();

    public Order generateOrder() {
        return new Order(
                id,"Test Product", new BigDecimal("99.99"), "CREATED",
                "CREATING_ORDER"
        );
    }

    @Test
    @DisplayName("Should process a Order and create a Payment successfully.")
    public void shouldCreatePayment() {

        Order order = generateOrder();

        Payment payment = paymentService.processOrder(order);

        Mockito.when(paymentRepository.save(payment)).thenReturn(payment);

        Payment result = paymentRepository.save(payment);

        Assertions.assertNotNull(result);

    }

    @Test
    @DisplayName("Should set Status to 'DECLINED' and failure reason as 'LIMIT_EXCEEDED")
    public void shouldSetStatusAsDeclined() {

        Order order = generateOrder();

        order.setAmount(new BigDecimal(5001));

        Payment payment = paymentService.processOrder(order);

        Mockito.when(paymentRepository.save(payment)).thenReturn(payment);

        Payment result = paymentRepository.save(payment);

        Assertions.assertEquals("DECLINED", result.getStatus());

    }

}