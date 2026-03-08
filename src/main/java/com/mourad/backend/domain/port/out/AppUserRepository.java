package com.mourad.backend.domain.port.out;

import com.mourad.backend.domain.model.AppUser;

public interface AppUserRepository {

    boolean existsByPhone(String phone);

    AppUser save(AppUser user);
}
