package com.mourad.backend.interfaces.rest;

import com.mourad.backend.domain.model.TokenPair;
import com.mourad.backend.domain.port.in.AuthUseCase;
import com.mourad.backend.domain.port.in.RegisterUseCase;
import com.mourad.backend.domain.port.in.UserAuthUseCase;
import com.mourad.backend.interfaces.dto.request.LoginRequest;
import com.mourad.backend.interfaces.dto.request.RefreshTokenRequest;
import com.mourad.backend.interfaces.dto.request.RegisterRequest;
import com.mourad.backend.interfaces.dto.request.UserLoginRequest;
import com.mourad.backend.interfaces.dto.response.ApiError;
import com.mourad.backend.interfaces.dto.response.AuthResponse;
import com.mourad.backend.interfaces.dto.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Authentication endpoints — admin and mobile app users")
public class AuthController {

    private final AuthUseCase authUseCase;
    private final RegisterUseCase registerUseCase;
    private final UserAuthUseCase userAuthUseCase;

    public AuthController(AuthUseCase authUseCase,
                          RegisterUseCase registerUseCase,
                          UserAuthUseCase userAuthUseCase) {
        this.authUseCase = authUseCase;
        this.registerUseCase = registerUseCase;
        this.userAuthUseCase = userAuthUseCase;
    }

    // ── ADMIN LOGIN ───────────────────────────────────────────────────────────

    @PostMapping("/login")
    @Operation(
            summary = "Admin login (username + password)",
            description = "Authenticates an admin with username and password. Returns an access token (1 h) and a refresh token (7 d). Default dev credentials: `admin / admin`."
    )
    @ApiResponse(responseCode = "200", description = "Authentication successful",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AuthResponse.class),
                    examples = @ExampleObject(name = "success", value = """
                            {
                              "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
                              "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
                              "tokenType": "Bearer"
                            }
                            """)))
    @ApiResponse(responseCode = "400", description = "Missing or blank field",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "401", description = "Wrong username or password",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenPair tokenPair = authUseCase.login(request.username(), request.password());
        return ResponseEntity.ok(AuthResponse.from(tokenPair));
    }

    // ── MOBILE USER REGISTRATION ──────────────────────────────────────────────

    @PostMapping("/register")
    @Operation(
            summary = "Register a new mobile app user",
            description = "Creates a new user account identified by phone number. Password is hashed with BCrypt. No JWT is issued — call POST /api/auth/login/user after registration."
    )
    @ApiResponse(responseCode = "201", description = "Account created successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = """
                            {"message": "Account created successfully"}
                            """)))
    @ApiResponse(responseCode = "400", description = "Missing, blank or invalid field",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2026-03-08T10:00:00.000Z",
                              "status": 400,
                              "error": "BAD_REQUEST",
                              "message": "password: must be at least 8 characters",
                              "path": "/api/auth/register"
                            }
                            """)))
    @ApiResponse(responseCode = "409", description = "Phone number already registered",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2026-03-08T10:00:00.000Z",
                              "status": 409,
                              "error": "CONFLICT",
                              "message": "A user with phone number '+212600123456' already exists",
                              "path": "/api/auth/register"
                            }
                            """)))
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest request) {
        registerUseCase.register(
                request.firstName().strip(),
                request.lastName().strip(),
                request.phone().strip(),
                request.password()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MessageResponse.of("Account created successfully"));
    }

    // ── MOBILE USER LOGIN ─────────────────────────────────────────────────────

    @PostMapping("/login/user")
    @Operation(
            summary = "Mobile user login (phone + password)",
            description = "Authenticates a mobile app user with phone and password. Returns an access token (1 h) and a refresh token (7 d). Use POST /api/auth/refresh/user to renew."
    )
    @ApiResponse(responseCode = "200", description = "Authentication successful",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AuthResponse.class),
                    examples = @ExampleObject(name = "success", value = """
                            {
                              "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
                              "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
                              "tokenType": "Bearer"
                            }
                            """)))
    @ApiResponse(responseCode = "400", description = "Missing or invalid field",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "401", description = "Wrong phone or password",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2026-03-08T10:00:00.000Z",
                              "status": 401,
                              "error": "UNAUTHORIZED",
                              "message": "Invalid phone number or password",
                              "path": "/api/auth/login/user"
                            }
                            """)))
    @ApiResponse(responseCode = "403", description = "Account is not yet active (status = PENDING)",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2026-03-08T10:00:00.000Z",
                              "status": 403,
                              "error": "FORBIDDEN",
                              "message": "Account is not yet active. Please wait for activation.",
                              "path": "/api/auth/login/user"
                            }
                            """)))
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody UserLoginRequest request) {
        TokenPair tokenPair = userAuthUseCase.login(request.phone().strip(), request.password());
        return ResponseEntity.ok(AuthResponse.from(tokenPair));
    }

    // ── MOBILE USER TOKEN REFRESH ─────────────────────────────────────────────

    @PostMapping("/refresh/user")
    @Operation(
            summary = "Refresh mobile user access token",
            description = "Exchanges a valid mobile-user refresh token for a new token pair."
    )
    @ApiResponse(responseCode = "200", description = "Token refreshed successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AuthResponse.class)))
    @ApiResponse(responseCode = "401", description = "Refresh token expired or invalid",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<AuthResponse> refreshUser(@Valid @RequestBody RefreshTokenRequest request) {
        TokenPair tokenPair = userAuthUseCase.refresh(request.refreshToken());
        return ResponseEntity.ok(AuthResponse.from(tokenPair));
    }

    // ── ADMIN TOKEN REFRESH ───────────────────────────────────────────────────

    @PostMapping("/refresh")
    @Operation(
            summary = "Refresh admin access token",
            description = "Exchanges a valid admin refresh token for a new token pair."
    )
    @ApiResponse(responseCode = "200", description = "Token refreshed successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AuthResponse.class)))
    @ApiResponse(responseCode = "401", description = "Refresh token expired or invalid",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        TokenPair tokenPair = authUseCase.refresh(request.refreshToken());
        return ResponseEntity.ok(AuthResponse.from(tokenPair));
    }
}
