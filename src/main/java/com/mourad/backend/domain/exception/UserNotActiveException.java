package com.mourad.backend.domain.exception;

public class UserNotActiveException extends RuntimeException {

    public UserNotActiveException() {
        super("Account is not yet active. Please wait for activation.");
    }
}
