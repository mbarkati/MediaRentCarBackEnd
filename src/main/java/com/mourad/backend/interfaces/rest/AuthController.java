package com.mourad.backend.interfaces.rest;

import com.mourad.backend.domain.model.TokenPair;
import com.mourad.backend.domain.port.in.AuthUseCase;
import com.mourad.backend.interfaces.dto.request.LoginRequest;
import com.mourad.backend.interfaces.dto.request.RefreshTokenRequest;
import com.mourad.backend.interfaces.dto.response.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Authentication endpoints")
public class AuthController {

    private final AuthUseCase authUseCase;

    public AuthController(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate and receive a JWT token pair")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenPair tokenPair = authUseCase.login(request.username(), request.password());
        return ResponseEntity.ok(AuthResponse.from(tokenPair));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Obtain a new access token using a valid refresh token")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        TokenPair tokenPair = authUseCase.refresh(request.refreshToken());
        return ResponseEntity.ok(AuthResponse.from(tokenPair));
    }
}
