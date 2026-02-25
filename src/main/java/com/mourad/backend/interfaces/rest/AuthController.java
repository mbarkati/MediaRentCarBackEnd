package com.mourad.backend.interfaces.rest;

import com.mourad.backend.domain.model.TokenPair;
import com.mourad.backend.domain.port.in.AuthUseCase;
import com.mourad.backend.interfaces.dto.request.LoginRequest;
import com.mourad.backend.interfaces.dto.request.RefreshTokenRequest;
import com.mourad.backend.interfaces.dto.response.ApiError;
import com.mourad.backend.interfaces.dto.response.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Obtain and renew JWT tokens")
public class AuthController {

    private final AuthUseCase authUseCase;

    public AuthController(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    // ── LOGIN ─────────────────────────────────────────────────────────────────

    @PostMapping("/login")
    @Operation(
            summary = "Login and receive a JWT token pair",
            description = "Authenticates an admin with username and password. Returns an access token (1 h) and a refresh token (7 d). Default dev credentials: `admin / admin`."
    )
    @ApiResponse(responseCode = "200", description = "Authentication successful",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AuthResponse.class),
                    examples = @ExampleObject(name = "success", value = """
                            {
                              "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInR5cGUiOiJBQ0NFU1MiLCJpYXQiOjE3NDA0OTU2NjcsImV4cCI6MTc0MDQ5OTI2N30.xxx",
                              "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInR5cGUiOiJSRUZSRVNIIiwiaWF0IjoxNzQwNDk1NjY3LCJleHAiOjE3NDExMDA0Njd9.yyy",
                              "tokenType": "Bearer"
                            }
                            """)))
    @ApiResponse(responseCode = "400", description = "Missing or blank field",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(name = "validation", value = """
                            {
                              "timestamp": "2026-02-25T20:00:00.000Z",
                              "status": 400,
                              "error": "BAD_REQUEST",
                              "message": "username: must not be blank",
                              "path": "/api/auth/login"
                            }
                            """)))
    @ApiResponse(responseCode = "401", description = "Wrong username or password",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(name = "bad-credentials", value = """
                            {
                              "timestamp": "2026-02-25T20:00:00.000Z",
                              "status": 401,
                              "error": "UNAUTHORIZED",
                              "message": "Invalid username or password",
                              "path": "/api/auth/login"
                            }
                            """)))
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenPair tokenPair = authUseCase.login(request.username(), request.password());
        return ResponseEntity.ok(AuthResponse.from(tokenPair));
    }

    // ── REFRESH ───────────────────────────────────────────────────────────────

    @PostMapping("/refresh")
    @Operation(
            summary = "Refresh the access token",
            description = "Exchanges a valid refresh token for a new token pair. The old refresh token is invalidated."
    )
    @ApiResponse(responseCode = "200", description = "Token refreshed successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AuthResponse.class)))
    @ApiResponse(responseCode = "401", description = "Refresh token expired or invalid",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2026-02-25T20:00:00.000Z",
                              "status": 401,
                              "error": "UNAUTHORIZED",
                              "message": "Refresh token is invalid or has expired",
                              "path": "/api/auth/refresh"
                            }
                            """)))
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        TokenPair tokenPair = authUseCase.refresh(request.refreshToken());
        return ResponseEntity.ok(AuthResponse.from(tokenPair));
    }
}
