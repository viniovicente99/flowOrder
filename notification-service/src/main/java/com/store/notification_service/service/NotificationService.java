package com.store.notification_service.service;

import com.store.notification_service.model.Notification;
import com.store.notification_service.model.Payment;
import com.store.notification_service.repository.NotificationRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Value("${rabbitmq.paymentqueue}")
    private String paymentQueue;

    @Autowired
    NotificationRepository notificationRepository;

    @RabbitListener( containerFactory = "listenerContainerFactory", queues = "${rabbitmq.paymentqueue}")
    public void receiveMessage (Payment payment) {

        System.out.println("New RMQ message received.");

        Notification notification = processNotification(payment);

        notificationRepository.save(notification);

    }

    public Notification processNotification(Payment payment) {

        Notification notification = new Notification();

        notification.setOrderId(payment.getOrderId());
        notification.setStatus(payment.getStatus());
        notification.setMessage(generateMessage(payment.getStatus()));

        return notification;

    }

    private String generateMessage(String status) {
        return switch (status) {
            case "DECLINED" -> "Payment refused";
            case "APPROVED" -> "Payment processed successfully";
            default -> "Unknown status";
        };
    }

}
