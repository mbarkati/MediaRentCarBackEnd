package com.mourad.backend.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "New WhatsApp phone number",
        example = """
                { "phone": "212612345678" }
                """)
public record UpdateWhatsappPhoneRequest(

        @Schema(description = "Phone number — digits only, 10-15 characters", example = "212612345678")
        @NotBlank(message = "Phone is required")
        String phone
) {}
