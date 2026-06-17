package com.store.payment_service.service;

import com.store.payment_service.model.Order;
import com.store.payment_service.model.Payment;
import com.store.payment_service.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    public void processPayment(Order order) {

        Payment payment = new Payment();

        payment.setOrderId(order.getId());
        payment.setAmount(order.getAmount());

        if (order.getAmount().compareTo(new BigDecimal(5000)) > 0) {
            order.setStatus("DECLINED");
            order.setStatusReason("LIMIT_EXCEEDED");
        } else {
            order.setStatus("APPROVED");
        }

        paymentRepository.save(payment);

    }

}
