package com.mourad.backend.domain.port.in;

import com.mourad.backend.domain.model.TokenPair;

public interface AuthUseCase {

    /** Verify credentials and return a token pair. */
    TokenPair login(String username, String rawPassword);

    /** Validate a refresh token and issue a fresh token pair. */
    TokenPair refresh(String refreshToken);
}
