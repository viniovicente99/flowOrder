package com.store.notification_service.service;

import com.store.notification_service.model.Notification;
import com.store.notification_service.model.Payment;
import com.store.notification_service.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    public void processNotification(Payment payment) {

        Notification notification = new Notification();

        notification.setOrderId(payment.getOrderId());
        notification.setStatus(payment.getStatus());
        notification.setMessage(generateMessage(payment.getStatus()));

        notificationRepository.save(notification);

    }

    private String generateMessage(String status) {
        return switch (status) {
            case "DECLINED" -> "Payment refused";
            case "APPROVED" -> "Payment processed successfully";
            default -> "Unknown status";
        };
    }

}
