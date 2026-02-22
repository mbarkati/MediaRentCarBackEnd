package com.mourad.backend.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain entity representing an admin user account.
 * Password is stored as a BCrypt hash — the domain holds it opaquely
 * and never re-hashes or decodes it.
 */
public class AdminAccount {

    private UUID id;
    private String username;
    private String hashedPassword;
    private LocalDateTime createdAt;

    private AdminAccount() {}

    public static AdminAccount create(String username, String hashedPassword) {
        AdminAccount account = new AdminAccount();
        account.id = UUID.randomUUID();
        account.username = username;
        account.hashedPassword = hashedPassword;
        account.createdAt = LocalDateTime.now();
        return account;
    }

    public static AdminAccount reconstitute(UUID id, String username,
                                             String hashedPassword, LocalDateTime createdAt) {
        AdminAccount account = new AdminAccount();
        account.id = id;
        account.username = username;
        account.hashedPassword = hashedPassword;
        account.createdAt = createdAt;
        return account;
    }

    public UUID getId() { return id; }
    public String getUsername() { return username; }
    public String getHashedPassword() { return hashedPassword; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
