package com.mourad.backend.application.service;

import com.mourad.backend.domain.exception.InvalidCredentialsException;
import com.mourad.backend.domain.model.AdminAccount;
import com.mourad.backend.domain.model.TokenPair;
import com.mourad.backend.domain.port.in.AuthUseCase;
import com.mourad.backend.domain.port.out.AdminAccountRepository;
import com.mourad.backend.domain.port.out.PasswordHashPort;
import com.mourad.backend.domain.port.out.TokenPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService implements AuthUseCase {

    private final AdminAccountRepository adminAccountRepository;
    private final PasswordHashPort passwordHashPort;
    private final TokenPort tokenPort;

    public AuthService(AdminAccountRepository adminAccountRepository,
                       PasswordHashPort passwordHashPort,
                       TokenPort tokenPort) {
        this.adminAccountRepository = adminAccountRepository;
        this.passwordHashPort = passwordHashPort;
        this.tokenPort = tokenPort;
    }

    @Override
    public TokenPair login(String username, String rawPassword) {
        AdminAccount admin = adminAccountRepository.findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordHashPort.matches(rawPassword, admin.getHashedPassword())) {
            throw new InvalidCredentialsException();
        }

        return new TokenPair(
                tokenPort.generateAccessToken(admin.getUsername()),
                tokenPort.generateRefreshToken(admin.getUsername())
        );
    }

    @Override
    public TokenPair refresh(String refreshToken) {
        if (!tokenPort.isRefreshTokenValid(refreshToken)) {
            throw new InvalidCredentialsException("Refresh token is invalid or has expired");
        }

        String username = tokenPort.extractUsernameFromToken(refreshToken);

        // Confirm the account still exists
        adminAccountRepository.findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);

        return new TokenPair(
                tokenPort.generateAccessToken(username),
                tokenPort.generateRefreshToken(username)
        );
    }
}
