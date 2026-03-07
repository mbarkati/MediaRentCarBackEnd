package com.mourad.backend.interfaces.dto.request;

import com.mourad.backend.domain.model.Fuel;
import com.mourad.backend.domain.model.Transmission;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Payload to fully update a car")
public record UpdateCarRequest(

        @Schema(description = "Manufacturer name", example = "Renault")
        @NotBlank(message = "Brand is required")
        String brand,

        @Schema(description = "Model name", example = "Clio")
        @NotBlank(message = "Model is required")
        String model,

        @Schema(description = "Manufacturing year — range [1980, currentYear+1]", example = "2023")
        @NotNull(message = "Year is required")
        @Min(value = 1980, message = "Year must be >= 1980")
        Integer year,

        @Schema(description = "Daily rental price — must be > 0", example = "300.00")
        @NotNull(message = "Price per day is required")
        @Positive(message = "Price per day must be greater than 0")
        BigDecimal pricePerDay,

        @Schema(description = "ISO 4217 currency code (3 letters)", example = "MAD")
        @NotBlank(message = "Currency is required")
        @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO 4217 code (e.g. MAD)")
        String currency,

        @Schema(description = "Number of seats (optional)", example = "5")
        @Positive(message = "Seats must be greater than 0")
        Integer seats,

        @Schema(description = "Transmission type (optional)", example = "AUTOMATIC")
        Transmission transmission,

        @Schema(description = "Fuel type (optional)", example = "PETROL")
        Fuel fuel,

        @Schema(description = "City where the car is available (optional)", example = "Marrakech")
        String city,

        @Schema(description = "URL of the car image (optional)", example = "https://example.com/car.jpg")
        String imageUrl
) {}
