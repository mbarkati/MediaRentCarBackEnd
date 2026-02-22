package com.mourad.backend.domain.exception;

public class CarAlreadyExistsException extends RuntimeException {

    public CarAlreadyExistsException(String licensePlate) {
        super("Car already exists with license plate: " + licensePlate);
    }
}
