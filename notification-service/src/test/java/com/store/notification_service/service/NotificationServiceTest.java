package com.store.notification_service.service;

import com.store.notification_service.model.Notification;
import com.store.notification_service.model.Payment;
import com.store.notification_service.repository.NotificationRepository;
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
class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    UUID id = UUID.randomUUID();

    public Payment generatePayment() {
        return new Payment(
                id, new BigDecimal("99.99"), "APPROVED",
                "CREATING_ORDER"
        );
    }

    @Test
    @DisplayName("Should process a Payment and create a Notification successfully.")
    public void shouldCreatePayment() {

        Payment payment = generatePayment();

        Notification notification = notificationService.processNotification(payment);

        Mockito.when(notificationRepository.save(notification)).thenReturn(notification);

        Notification result = notificationRepository.save(notification);

        Assertions.assertNotNull(result);

    }

    @Test
    @DisplayName("Should set Message to 'Payment refused'.")
    public void shouldSetMessageAsRefused() {

        Payment payment = generatePayment();

        payment.setStatus("DECLINED");

        Notification notification = notificationService.processNotification(payment);

        Mockito.when(notificationRepository.save(notification)).thenReturn(notification);

        Notification result = notificationRepository.save(notification);

        Assertions.assertNotNull(result);

        Assertions.assertEquals("Payment refused", result.getMessage());

        System.out.println(result);

    }

    @Test
    @DisplayName("Should set message to 'Unknown status.")
    public void shouldSetMessageToUnknown() {

        Payment payment = generatePayment();

        payment.setStatus("TEST");

        Notification notification = notificationService.processNotification(payment);

        Mockito.when(notificationRepository.save(notification)).thenReturn(notification);

        Notification result = notificationRepository.save(notification);

        Assertions.assertNotNull(result);

        Assertions.assertEquals("Unknown status", result.getMessage());

        System.out.println(result);

    }

}