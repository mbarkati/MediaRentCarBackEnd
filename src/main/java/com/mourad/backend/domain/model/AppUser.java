package com.mourad.backend.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain entity representing a mobile app user (client).
 * Distinct from AdminAccount — identified by phone number.
 * Password is stored as a BCrypt hash held opaquely.
 */
public class AppUser {

    private UUID id;
    private String firstName;
    private String lastName;
    private String phone;
    private String passwordHash;
    private String status;
    private LocalDateTime createdAt;

    private AppUser() {}

    public static AppUser create(String firstName, String lastName,
                                 String phone, String passwordHash) {
        AppUser user = new AppUser();
        user.id = UUID.randomUUID();
        user.firstName = firstName;
        user.lastName = lastName;
        user.phone = phone;
        user.passwordHash = passwordHash;
        user.status = "ACTIVE";
        user.createdAt = LocalDateTime.now();
        return user;
    }

    public static AppUser reconstitute(UUID id, String firstName, String lastName,
                                       String phone, String passwordHash,
                                       String status, LocalDateTime createdAt) {
        AppUser user = new AppUser();
        user.id = id;
        user.firstName = firstName;
        user.lastName = lastName;
        user.phone = phone;
        user.passwordHash = passwordHash;
        user.status = status;
        user.createdAt = createdAt;
        return user;
    }

    public UUID getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhone() { return phone; }
    public String getPasswordHash() { return passwordHash; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
