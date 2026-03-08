package com.mourad.backend.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Mobile app user login credentials",
        example = """
                {"phone": "+212600123456", "password": "mypassword123"}
                """)
public record UserLoginRequest(

        @Schema(description = "Registered phone number", example = "+212600123456")
        @NotBlank
        @Pattern(
                regexp = "^\\+?[0-9]{7,15}$",
                message = "must be a valid phone number (7–15 digits, optional leading +)"
        )
        String phone,

        @Schema(description = "Account password", example = "mypassword123")
        @NotBlank
        String password
) {}
