package com.mourad.backend.application.service;

import com.mourad.backend.domain.exception.InvalidCredentialsException;
import com.mourad.backend.domain.exception.UserNotFoundException;
import com.mourad.backend.domain.model.AppUser;
import com.mourad.backend.domain.port.in.DeleteAccountUseCase;
import com.mourad.backend.domain.port.out.AppUserRepository;
import com.mourad.backend.domain.port.out.PasswordHashPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteAccountService implements DeleteAccountUseCase {

    private final AppUserRepository appUserRepository;
    private final PasswordHashPort passwordHashPort;

    public DeleteAccountService(AppUserRepository appUserRepository,
                                PasswordHashPort passwordHashPort) {
        this.appUserRepository = appUserRepository;
        this.passwordHashPort = passwordHashPort;
    }

    @Override
    @Transactional
    public void deleteByPhone(String phone) {
        if (!appUserRepository.existsByPhone(phone)) {
            throw new UserNotFoundException(phone);
        }
        appUserRepository.deleteByPhone(phone);
    }

    @Override
    @Transactional
    public void verifyAndDelete(String phone, String rawPassword) {
        AppUser user = appUserRepository.findByPhone(phone)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid phone number or password"));

        if (!passwordHashPort.matches(rawPassword, user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid phone number or password");
        }

        appUserRepository.deleteByPhone(phone);
    }
}
