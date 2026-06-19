package com.store.payment_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Order {

    public Order(UUID id, String productName, BigDecimal amount, String status, String statusReason) {
        this.id = id;
        this.productName = productName;
        this.amount = amount;
        this.status = status;
        this.statusReason = statusReason;
    }

    public Order() {}

    private UUID id;

    @JsonProperty("product_name")
    @NotBlank(message = "Product name cannot be empty.")
    @Size(min = 3, max = 255, message = "Product name must be between 3 and 255 characters.")
    private String productName;

    @NotNull(message = "Amount is required.")
    @DecimalMin(value = "0.01", message = "Amount must be at least 0.01.")
    @DecimalMax(value = "999999.99", message = "Amount must be less than 1,000,000.00.")
    private BigDecimal amount;

    @NotBlank(message = "Status cannot be empty.")
    @Size(min = 2, max = 50, message = "Status must be between 2 and 50 characters.")
    private String status;

    @JsonProperty("status_reason")
    private String statusReason;

    @JsonProperty("created_at")
    @JsonIgnoreProperties
    private LocalDateTime createdAt;

}
