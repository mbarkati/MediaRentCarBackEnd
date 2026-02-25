package com.mourad.backend.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Payload to update the daily rental price of a car",
        example = """
                {"dailyPrice": 60.00, "currency": "EUR"}
                """)
public record UpdateCarPriceRequest(

        @Schema(description = "New daily rental price — must be > 0", example = "60.00")
        @NotNull(message = "Daily price is required")
        @Positive(message = "Daily price must be greater than 0")
        BigDecimal dailyPrice,

        @Schema(description = "ISO 4217 currency code (3 letters)", example = "EUR")
        @NotBlank(message = "Currency is required")
        @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO 4217 code (e.g. EUR)")
        String currency
) {}
