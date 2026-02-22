package com.mourad.backend.domain.port.out;

/**
 * Port for JWT token generation and validation.
 * Keeps JJWT (an infrastructure concern) out of the application layer.
 */
public interface TokenPort {

    String generateAccessToken(String username);

    String generateRefreshToken(String username);

    /** Extract username from either token type (does not validate expiry or type). */
    String extractUsernameFromToken(String token);

    boolean isAccessTokenValid(String token);

    boolean isRefreshTokenValid(String token);
}
