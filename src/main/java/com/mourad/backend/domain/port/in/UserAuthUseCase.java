package com.mourad.backend.domain.port.in;

import com.mourad.backend.domain.model.TokenPair;

public interface UserAuthUseCase {

    /** Authenticate a mobile user with phone + password and return a token pair. */
    TokenPair login(String phone, String rawPassword);

    /** Validate a mobile-user refresh token and issue a fresh token pair. */
    TokenPair refresh(String refreshToken);
}
