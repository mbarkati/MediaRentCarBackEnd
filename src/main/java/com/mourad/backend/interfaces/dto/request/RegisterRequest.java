package com.mourad.backend.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Mobile app user registration payload",
        example = """
                {"firstName": "Mourad", "lastName": "Barkati", "phone": "+212600123456"}
                """)
public record RegisterRequest(

        @Schema(description = "First name", example = "Mourad")
        @NotBlank
        String firstName,

        @Schema(description = "Last name", example = "Barkati")
        @NotBlank
        String lastName,

        @Schema(description = "Phone number — 7 to 15 digits, optional leading +", example = "+212600123456")
        @NotBlank
        @Pattern(
                regexp = "^\\+?[0-9]{7,15}$",
                message = "must be a valid phone number (7–15 digits, optional leading +)"
        )
        String phone
) {}
