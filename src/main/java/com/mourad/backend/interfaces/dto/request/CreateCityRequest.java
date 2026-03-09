package com.mourad.backend.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload to create a new city",
        example = """
                {"name": "Casablanca Centre Ville"}
                """)
public record CreateCityRequest(

        @Schema(description = "City name — max 100 characters", example = "Casablanca Centre Ville")
        @NotBlank
        @Size(max = 100, message = "must not exceed 100 characters")
        String name
) {}
