package com.store.notification_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "notifications")
public class Notification {

    public Notification(UUID id, UUID orderId, String message, String status, LocalDateTime createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.message = message;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Notification() {}

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "order_id")
    @JsonProperty("order_id")
    @NotNull(message = "Order Id is required.")
    private UUID orderId;

    @NotBlank(message = "Message cannot be empty.")
    @Size(min = 2, max = 50, message = "Message must be between 2 and 50 characters.")
    private String message;

    @NotBlank(message = "Status cannot be empty.")
    @Size(min = 2, max = 50, message = "Status must be between 2 and 50 characters.")
    private String status;

    @Column(name = "created_at")
    @JsonProperty("created_at")
    @UpdateTimestamp
    private LocalDateTime createdAt;
}
