package com.mourad.backend.interfaces.dto.response;

import com.mourad.backend.domain.model.TokenPair;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "JWT token pair returned after a successful authentication")
public record AuthResponse(

        @Schema(description = "Short-lived access token (default: 1 h)",
                example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInR5cGUiOiJBQ0NFU1MifQ.xxx")
        String accessToken,

        @Schema(description = "Long-lived refresh token (default: 7 d)",
                example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInR5cGUiOiJSRUZSRVNIIn0.yyy")
        String refreshToken,

        @Schema(description = "Token type — always 'Bearer'", example = "Bearer")
        String tokenType
) {
    public static AuthResponse from(TokenPair tokenPair) {
        return new AuthResponse(tokenPair.accessToken(), tokenPair.refreshToken(), "Bearer");
    }
}
