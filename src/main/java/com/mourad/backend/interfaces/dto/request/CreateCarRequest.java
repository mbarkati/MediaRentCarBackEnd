package com.mourad.backend.interfaces.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Input for POST /api/v1/cars.
 * Bean Validation provides a first line of defence; domain invariants
 * (year ceiling = currentYear+1, ISO 4217 currency) are enforced in Car.create().
 */
public record CreateCarRequest(

        @NotBlank(message = "Brand is required")
        String brand,

        @NotBlank(message = "Model is required")
        String model,

        @NotNull(message = "Year is required")
        @Min(value = 1980, message = "Year must be >= 1980")
        Integer year,

        @NotNull(message = "Daily price is required")
        @Positive(message = "Daily price must be greater than 0")
        BigDecimal dailyPrice,

        @NotBlank(message = "Currency is required")
        @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO 4217 code (e.g. EUR)")
        String currency
) {}
