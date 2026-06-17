package com.store.notification_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class Payment {

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

    @JsonProperty("failure_reason")
    private String failureReason;

}
