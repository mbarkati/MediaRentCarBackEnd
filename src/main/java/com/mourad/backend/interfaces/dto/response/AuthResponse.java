package com.mourad.backend.interfaces.dto.response;

import com.mourad.backend.domain.model.TokenPair;

public record AuthResponse(String accessToken, String refreshToken, String tokenType) {

    public static AuthResponse from(TokenPair tokenPair) {
        return new AuthResponse(tokenPair.accessToken(), tokenPair.refreshToken(), "Bearer");
    }
}
