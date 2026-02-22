package com.mourad.backend.domain.port.out;

/**
 * Port for password hashing operations.
 * Keeps BCrypt (an infrastructure concern) out of the domain and application layers.
 */
public interface PasswordHashPort {

    String hash(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}
