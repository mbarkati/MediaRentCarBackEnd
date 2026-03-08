package com.mourad.backend.domain.port.in;

public interface RegisterUseCase {

    /** Register a new mobile app user identified by phone number. */
    void register(String firstName, String lastName, String phone);
}
