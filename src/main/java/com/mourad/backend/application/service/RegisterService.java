package com.mourad.backend.application.service;

import com.mourad.backend.domain.exception.UserAlreadyExistsException;
import com.mourad.backend.domain.model.AppUser;
import com.mourad.backend.domain.port.in.RegisterUseCase;
import com.mourad.backend.domain.port.out.AppUserRepository;
import com.mourad.backend.domain.port.out.PasswordHashPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterService implements RegisterUseCase {

    private final AppUserRepository appUserRepository;
    private final PasswordHashPort passwordHashPort;

    public RegisterService(AppUserRepository appUserRepository, PasswordHashPort passwordHashPort) {
        this.appUserRepository = appUserRepository;
        this.passwordHashPort = passwordHashPort;
    }

    @Override
    @Transactional
    public void register(String firstName, String lastName, String phone, String rawPassword) {
        if (appUserRepository.existsByPhone(phone)) {
            throw new UserAlreadyExistsException(phone);
        }
        String passwordHash = passwordHashPort.hash(rawPassword);
        appUserRepository.save(AppUser.create(firstName, lastName, phone, passwordHash));
    }
}
