package com.mourad.backend.interfaces.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Input for PATCH /api/v1/cars/{id}/price — focused price update.
 * Changing only the price (and optionally the currency) is a common
 * admin operation that deserves its own intentional command.
 */
public record UpdateCarPriceRequest(

        @NotNull(message = "Daily price is required")
        @Positive(message = "Daily price must be greater than 0")
        BigDecimal dailyPrice,

        @NotBlank(message = "Currency is required")
        @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO 4217 code (e.g. EUR)")
        String currency
) {}
