package com.mourad.backend.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Mobile app user registration payload",
        example = """
                {"firstName": "Mourad", "lastName": "Barkati", "phone": "+212600123456", "password": "mypassword123"}
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
        String phone,

        @Schema(description = "Password — minimum 8 characters", example = "mypassword123")
        @NotBlank
        @Size(min = 8, message = "must be at least 8 characters")
        String password
) {}
