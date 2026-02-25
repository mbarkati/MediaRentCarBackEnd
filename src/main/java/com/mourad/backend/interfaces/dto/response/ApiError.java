package com.mourad.backend.interfaces.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Schema(description = "Standard error envelope returned by all error responses")
public record ApiError(

        @Schema(description = "ISO-8601 UTC timestamp of the error",
                example = "2026-02-25T20:34:27.794Z")
        String timestamp,

        @Schema(description = "HTTP status code", example = "404")
        int status,

        @Schema(description = "HTTP status name", example = "NOT_FOUND")
        String error,

        @Schema(description = "Human-readable error description",
                example = "Car not found with id: 550e8400-e29b-41d4-a716-446655440000")
        String message,

        @Schema(description = "Request URI that triggered the error",
                example = "/api/admin/cars/550e8400-e29b-41d4-a716-446655440000")
        String path
) {
    public static ApiError of(HttpStatus httpStatus, String message, String path) {
        return new ApiError(
                Instant.now().toString(),
                httpStatus.value(),
                httpStatus.name(),
                message,
                path);
    }
}
