package com.mourad.backend.interfaces.dto.response;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ApiError(
        String timestamp,
        int status,
        String error,
        String message,
        String path) {

    public static ApiError of(HttpStatus httpStatus, String message, String path) {
        return new ApiError(
                Instant.now().toString(),
                httpStatus.value(),
                httpStatus.name(),
                message,
                path);
    }
}
