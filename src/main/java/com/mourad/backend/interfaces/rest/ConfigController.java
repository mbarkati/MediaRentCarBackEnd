package com.mourad.backend.interfaces.rest;

import com.mourad.backend.application.dto.WhatsappPhoneDto;
import com.mourad.backend.application.port.in.GetWhatsappPhoneUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/config")
@Tag(name = "Public Config", description = "Public app configuration — no authentication required")
public class ConfigController {

    private final GetWhatsappPhoneUseCase getWhatsappPhoneUseCase;

    public ConfigController(GetWhatsappPhoneUseCase getWhatsappPhoneUseCase) {
        this.getWhatsappPhoneUseCase = getWhatsappPhoneUseCase;
    }

    @GetMapping("/whatsapp-phone")
    @Operation(summary = "Get the WhatsApp booking phone number")
    @ApiResponse(responseCode = "200", description = "Current WhatsApp number",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            { "phone": "212600851481" }
                            """)))
    public ResponseEntity<WhatsappPhoneDto> getWhatsappPhone() {
        return ResponseEntity.ok(getWhatsappPhoneUseCase.execute());
    }
}
