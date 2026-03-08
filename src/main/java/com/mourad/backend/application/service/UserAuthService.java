package com.mourad.backend.application.service;

import com.mourad.backend.domain.exception.InvalidCredentialsException;
import com.mourad.backend.domain.exception.UserNotActiveException;
import com.mourad.backend.domain.model.AppUser;
import com.mourad.backend.domain.model.TokenPair;
import com.mourad.backend.domain.port.in.UserAuthUseCase;
import com.mourad.backend.domain.port.out.AppUserRepository;
import com.mourad.backend.domain.port.out.PasswordHashPort;
import com.mourad.backend.domain.port.out.TokenPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserAuthService implements UserAuthUseCase {

    private final AppUserRepository appUserRepository;
    private final PasswordHashPort passwordHashPort;
    private final TokenPort tokenPort;

    public UserAuthService(AppUserRepository appUserRepository,
                           PasswordHashPort passwordHashPort,
                           TokenPort tokenPort) {
        this.appUserRepository = appUserRepository;
        this.passwordHashPort = passwordHashPort;
        this.tokenPort = tokenPort;
    }

    @Override
    public TokenPair login(String phone, String rawPassword) {
        AppUser user = appUserRepository.findByPhone(phone)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid phone number or password"));

        if (!passwordHashPort.matches(rawPassword, user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid phone number or password");
        }

        if ("PENDING".equals(user.getStatus())) {
            throw new UserNotActiveException();
        }

        return new TokenPair(
                tokenPort.generateAccessToken(user.getPhone()),
                tokenPort.generateRefreshToken(user.getPhone())
        );
    }

    @Override
    public TokenPair refresh(String refreshToken) {
        if (!tokenPort.isRefreshTokenValid(refreshToken)) {
            throw new InvalidCredentialsException("Refresh token is invalid or has expired");
        }

        String phone = tokenPort.extractUsernameFromToken(refreshToken);

        appUserRepository.findByPhone(phone)
                .orElseThrow(() -> new InvalidCredentialsException("Refresh token is invalid or has expired"));

        return new TokenPair(
                tokenPort.generateAccessToken(phone),
                tokenPort.generateRefreshToken(phone)
        );
    }
}
