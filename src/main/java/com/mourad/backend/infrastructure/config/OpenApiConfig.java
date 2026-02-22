package com.mourad.backend.infrastructure.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER,
        description = "JWT token — format: Bearer <token>"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI carRentalOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Car Rental API")
                        .description("RESTful backend for car rental management — Hexagonal Architecture")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Mourad Barkati")))
                // Apply JWT auth requirement globally to all endpoints
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
