package com.mourad.backend.domain.port.out;

import com.mourad.backend.domain.model.AdminAccount;

import java.util.Optional;

public interface AdminAccountRepository {

    Optional<AdminAccount> findByUsername(String username);

    boolean existsByUsername(String username);

    AdminAccount save(AdminAccount adminAccount);
}
