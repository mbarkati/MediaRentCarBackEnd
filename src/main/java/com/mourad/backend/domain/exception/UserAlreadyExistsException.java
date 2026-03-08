package com.mourad.backend.domain.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String phone) {
        super("A user with phone number '" + phone + "' already exists");
    }
}
