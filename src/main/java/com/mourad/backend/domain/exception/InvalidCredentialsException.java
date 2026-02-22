package com.mourad.backend.domain.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Invalid username or password");
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
