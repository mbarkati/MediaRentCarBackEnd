package com.mourad.backend.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Payload to add a car to the fleet",
        example = """
                {
                  "brand": "Peugeot",
                  "model": "308",
                  "year": 2023,
                  "dailyPrice": 55.00,
                  "currency": "EUR"
                }
                """)
public record CreateCarRequest(

        @Schema(description = "Manufacturer name", example = "Peugeot")
        @NotBlank(message = "Brand is required")
        String brand,

        @Schema(description = "Model name", example = "308")
        @NotBlank(message = "Model is required")
        String model,

        @Schema(description = "Manufacturing year — range [1980, currentYear+1]", example = "2023")
        @NotNull(message = "Year is required")
        @Min(value = 1980, message = "Year must be >= 1980")
        Integer year,

        @Schema(description = "Daily rental price — must be > 0", example = "55.00")
        @NotNull(message = "Daily price is required")
        @Positive(message = "Daily price must be greater than 0")
        BigDecimal dailyPrice,

        @Schema(description = "ISO 4217 currency code (3 letters)", example = "EUR")
        @NotBlank(message = "Currency is required")
        @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO 4217 code (e.g. EUR)")
        String currency
) {}
