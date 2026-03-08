package com.mourad.backend.domain.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String phone) {
        super("No user found for phone: " + phone);
    }
}
