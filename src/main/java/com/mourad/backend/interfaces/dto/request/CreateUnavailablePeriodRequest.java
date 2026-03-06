package com.mourad.backend.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "Payload to block a car for a date range",
        example = """
                {
                  "startDate": "2026-03-10",
                  "endDate": "2026-03-15",
                  "reason": "En maintenance"
                }
                """)
public record CreateUnavailablePeriodRequest(

        @Schema(description = "First unavailable date (inclusive)", example = "2026-03-10")
        @NotNull(message = "Start date is required")
        LocalDate startDate,

        @Schema(description = "Last unavailable date (inclusive)", example = "2026-03-15")
        @NotNull(message = "End date is required")
        LocalDate endDate,

        @Schema(description = "Optional reason", example = "En maintenance")
        String reason
) {}
