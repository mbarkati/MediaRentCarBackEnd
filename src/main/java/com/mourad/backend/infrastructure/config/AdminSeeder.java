package com.mourad.backend.infrastructure.config;

import com.mourad.backend.domain.model.AdminAccount;
import com.mourad.backend.domain.port.out.AdminAccountRepository;
import com.mourad.backend.domain.port.out.PasswordHashPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Seeds the default admin account on every startup (idempotent).
 * Credentials: username=admin / password=admin
 */
@Component
public class AdminSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminSeeder.class);

    private final AdminAccountRepository adminAccountRepository;
    private final PasswordHashPort passwordHashPort;

    public AdminSeeder(AdminAccountRepository adminAccountRepository,
                       PasswordHashPort passwordHashPort) {
        this.adminAccountRepository = adminAccountRepository;
        this.passwordHashPort = passwordHashPort;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (adminAccountRepository.existsByUsername("admin")) {
            log.debug("Admin account already exists — skipping seed.");
            return;
        }
        AdminAccount admin = AdminAccount.create("admin", passwordHashPort.hash("admin"));
        adminAccountRepository.save(admin);
        log.info("Default admin account seeded (username: admin).");
    }
}
