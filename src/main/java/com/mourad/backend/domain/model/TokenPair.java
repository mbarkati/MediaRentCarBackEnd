package com.mourad.backend.domain.model;

/**
 * Value object returned on successful authentication.
 * accessToken  — short-lived (default 1 h), sent with every API request.
 * refreshToken — long-lived (default 7 d), used only to obtain a new access token.
 */
public record TokenPair(String accessToken, String refreshToken) {}
