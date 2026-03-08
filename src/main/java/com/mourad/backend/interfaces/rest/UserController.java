package com.mourad.backend.interfaces.rest;

import com.mourad.backend.domain.port.in.DeleteAccountUseCase;
import com.mourad.backend.interfaces.dto.response.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "Mobile app user account management")
public class UserController {

    private final DeleteAccountUseCase deleteAccountUseCase;

    public UserController(DeleteAccountUseCase deleteAccountUseCase) {
        this.deleteAccountUseCase = deleteAccountUseCase;
    }

    @DeleteMapping("/me")
    @Operation(
            summary = "Delete the authenticated user's account",
            description = "Permanently deletes the account bound to the Bearer token. " +
                          "Returns 204 on success. After deletion all existing tokens for " +
                          "this user are effectively revoked (any subsequent request will get 401).",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "204", description = "Account deleted successfully")
    @ApiResponse(responseCode = "401", description = "Missing or invalid Bearer token",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "404", description = "User account not found (already deleted)",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<Void> deleteMe(Authentication authentication) {
        String phone = authentication.getName();
        deleteAccountUseCase.deleteByPhone(phone);
        return ResponseEntity.noContent().build();
    }
}
