package com.mourad.backend.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Admin login credentials",
        example = """
                {"username": "admin", "password": "admin"}
                """)
public record LoginRequest(

        @Schema(description = "Admin username", example = "admin")
        @NotBlank String username,

        @Schema(description = "Admin password", example = "admin")
        @NotBlank String password
) {}
