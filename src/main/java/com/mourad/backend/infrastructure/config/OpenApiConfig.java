package com.mourad.backend.infrastructure.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER,
        description = "Paste your access token here — format: `Bearer <token>`"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI carRentalOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Car Rental API")
                        .description("""
                                RESTful backend for a car rental platform — **Hexagonal Architecture**.

                                **Public routes** (`/api/cars/**`, `/api/auth/**`) require no authentication.

                                **Admin routes** (`/api/admin/**`) require a Bearer JWT token.\s
                                Obtain one via `POST /api/auth/login` (default dev credentials: `admin / admin`).
                                """)
                        .version("1.0.0")
                        .contact(new Contact().name("Mourad Barkati")));
        // Security is declared per-controller via @SecurityRequirement, not globally,
        // so public endpoints (/api/auth, /api/cars) do not show a lock icon.
    }
}
