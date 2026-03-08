package com.mourad.backend.application.service;

import com.mourad.backend.domain.exception.UserAlreadyExistsException;
import com.mourad.backend.domain.model.AppUser;
import com.mourad.backend.domain.port.in.RegisterUseCase;
import com.mourad.backend.domain.port.out.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterService implements RegisterUseCase {

    private final AppUserRepository appUserRepository;

    public RegisterService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    @Transactional
    public void register(String firstName, String lastName, String phone) {
        if (appUserRepository.existsByPhone(phone)) {
            throw new UserAlreadyExistsException(phone);
        }
        appUserRepository.save(AppUser.create(firstName, lastName, phone));
    }
}
