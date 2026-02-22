package com.mourad.backend.interfaces.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateCarRequest(
        @NotBlank(message = "License plate is required")
        String licensePlate,

        @NotBlank(message = "Brand is required")
        String brand,

        @NotBlank(message = "Model is required")
        String model,

        @NotNull(message = "Daily rate amount is required")
        @Positive(message = "Daily rate amount must be positive")
        BigDecimal dailyRateAmount,

        @NotBlank(message = "Daily rate currency is required")
        String dailyRateCurrency
) {}
