package com.mourad.backend.domain.exception;

import java.util.UUID;

public class CarNotFoundException extends RuntimeException {

    public CarNotFoundException(UUID id) {
        super("Car not found with id: " + id);
    }

    public CarNotFoundException(String licensePlate) {
        super("Car not found with license plate: " + licensePlate);
    }
}
