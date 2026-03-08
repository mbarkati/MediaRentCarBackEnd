package com.mourad.backend.domain.port.out;

import com.mourad.backend.domain.model.AppUser;

import java.util.Optional;

public interface AppUserRepository {

    boolean existsByPhone(String phone);

    Optional<AppUser> findByPhone(String phone);

    void deleteByPhone(String phone);

    AppUser save(AppUser user);
}
