package com.mourad.backend.interfaces.rest;

import com.mourad.backend.application.command.UpdateWhatsappPhoneCommand;
import com.mourad.backend.application.dto.WhatsappPhoneDto;
import com.mourad.backend.application.port.in.UpdateWhatsappPhoneUseCase;
import com.mourad.backend.interfaces.dto.request.UpdateWhatsappPhoneRequest;
import com.mourad.backend.interfaces.dto.response.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/config")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin Config", description = "App configuration management — requires ADMIN role")
public class AdminConfigController {

    private final UpdateWhatsappPhoneUseCase updateWhatsappPhoneUseCase;

    public AdminConfigController(UpdateWhatsappPhoneUseCase updateWhatsappPhoneUseCase) {
        this.updateWhatsappPhoneUseCase = updateWhatsappPhoneUseCase;
    }

    @PutMapping("/whatsapp-phone")
    @Operation(summary = "Update the WhatsApp booking phone number")
    @ApiResponse(responseCode = "200", description = "Phone updated",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            { "phone": "212612345678" }
                            """)))
    @ApiResponse(responseCode = "400", description = "Blank phone",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "422", description = "Invalid phone format or length",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = """
                            {
                              "status": 422,
                              "error": "UNPROCESSABLE_ENTITY",
                              "message": "Phone number must contain digits only (no +, spaces or dashes)"
                            }
                            """)))
    public ResponseEntity<WhatsappPhoneDto> updateWhatsappPhone(
            @Valid @RequestBody UpdateWhatsappPhoneRequest request) {
        return ResponseEntity.ok(
                updateWhatsappPhoneUseCase.execute(new UpdateWhatsappPhoneCommand(request.phone())));
    }
}
