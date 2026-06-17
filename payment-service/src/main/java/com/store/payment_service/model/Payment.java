package com.store.payment_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "payments")
public class Payment {

    public Payment(UUID orderId, BigDecimal amount, String status, String failureReason) {
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.failureReason = failureReason;
    }

    public Payment() {}

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "order_id")
    @JsonProperty("order_id")
    @NotNull(message = "Order Id is required.")
    private UUID orderId;

    @NotNull(message = "Amount is required.")
    @DecimalMin(value = "0.01", message = "Amount must be at least 0.01.")
    @DecimalMax(value = "999999.99", message = "Amount must be less than 1,000,000.00.")
    private BigDecimal amount;

    @NotBlank(message = "Status cannot be empty.")
    @Size(min = 2, max = 50, message = "Status must be between 2 and 50 characters.")
    private String status;

    @Column(name = "failure_reason")
    @JsonProperty("failure_reason")
    private String failureReason;

    @Column(name = "processed_at")
    @JsonProperty("processed_at")
    @UpdateTimestamp
    private LocalDateTime processedAt;

}
