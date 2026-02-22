package com.mourad.backend.infrastructure.security;

import com.mourad.backend.domain.port.out.TokenPort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider implements TokenPort {

    private static final String TYPE_CLAIM = "type";
    private static final String ACCESS = "ACCESS";
    private static final String REFRESH = "REFRESH";

    private final SecretKey secretKey;
    private final long accessExpirationMs;
    private final long refreshExpirationMs;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-expiration-ms:3600000}") long accessExpirationMs,
            @Value("${jwt.refresh-expiration-ms:604800000}") long refreshExpirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpirationMs = accessExpirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    @Override
    public String generateAccessToken(String username) {
        return buildToken(username, accessExpirationMs, ACCESS);
    }

    @Override
    public String generateRefreshToken(String username) {
        return buildToken(username, refreshExpirationMs, REFRESH);
    }

    @Override
    public String extractUsernameFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    @Override
    public boolean isAccessTokenValid(String token) {
        return isTokenValidWithType(token, ACCESS);
    }

    @Override
    public boolean isRefreshTokenValid(String token) {
        return isTokenValidWithType(token, REFRESH);
    }

    private String buildToken(String username, long expirationMs, String type) {
        return Jwts.builder()
                .subject(username)
                .claim(TYPE_CLAIM, type)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey)
                .compact();
    }

    private boolean isTokenValidWithType(String token, String expectedType) {
        try {
            Claims claims = parseClaims(token);
            return expectedType.equals(claims.get(TYPE_CLAIM, String.class));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
