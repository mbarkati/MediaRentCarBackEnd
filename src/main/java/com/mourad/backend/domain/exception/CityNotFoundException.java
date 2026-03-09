package com.mourad.backend.domain.exception;

import java.util.UUID;

public class CityNotFoundException extends RuntimeException {

    public CityNotFoundException(UUID id) {
        super("City not found with id: " + id);
    }
}
