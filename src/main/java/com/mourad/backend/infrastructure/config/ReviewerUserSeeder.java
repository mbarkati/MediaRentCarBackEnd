package com.mourad.backend.infrastructure.config;

import com.mourad.backend.domain.model.AppUser;
import com.mourad.backend.domain.port.out.AppUserRepository;
import com.mourad.backend.domain.port.out.PasswordHashPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Seeds a pre-approved reviewer account on every startup (idempotent).
 *
 * This account is intended for App Store Connect / Google Play Console reviewers
 * who need a ready-to-use ACTIVE account without having to self-register.
 *
 * Credentials are driven by environment variables:
 *   REVIEWER_PHONE    (default: +10000000001)
 *   REVIEWER_PASSWORD (default: Reviewer@2024)
 *
 * Include these credentials in the "Notes for Reviewer" field when submitting
 * to App Store Connect and Google Play Console.
 */
@Component
@Order(2)   // runs after AdminSeeder (Order 1 by default)
public class ReviewerUserSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ReviewerUserSeeder.class);

    private final AppUserRepository appUserRepository;
    private final PasswordHashPort passwordHashPort;

    @Value("${reviewer.user.phone}")
    private String reviewerPhone;

    @Value("${reviewer.user.password}")
    private String reviewerPassword;

    public ReviewerUserSeeder(AppUserRepository appUserRepository,
                              PasswordHashPort passwordHashPort) {
        this.appUserRepository = appUserRepository;
        this.passwordHashPort = passwordHashPort;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (appUserRepository.existsByPhone(reviewerPhone)) {
            log.info("Reviewer account already exists (phone: {}) — skipping seed.", reviewerPhone);
            return;
        }

        AppUser reviewer = AppUser.create(
                "Reviewer",
                "Test",
                reviewerPhone,
                passwordHashPort.hash(reviewerPassword)
        );
        appUserRepository.save(reviewer);

        log.info("=====================================================");
        log.info("  Reviewer test account seeded (status: ACTIVE)");
        log.info("  Phone   : {}", reviewerPhone);
        log.info("  Password: {}", reviewerPassword);
        log.info("  Use these credentials in App Store / Play Console");
        log.info("  'Notes for Reviewer' when submitting a new build.");
        log.info("=====================================================");
    }
}
