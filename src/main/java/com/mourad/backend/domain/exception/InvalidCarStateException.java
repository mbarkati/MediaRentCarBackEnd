package com.mourad.backend.domain.exception;

public class InvalidCarStateException extends RuntimeException {

    public InvalidCarStateException(String message) {
        super(message);
    }
}
