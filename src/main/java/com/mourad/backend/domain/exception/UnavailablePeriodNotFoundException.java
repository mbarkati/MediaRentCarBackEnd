package com.mourad.backend.domain.exception;

import java.util.UUID;

public class UnavailablePeriodNotFoundException extends RuntimeException {

    public UnavailablePeriodNotFoundException(UUID id) {
        super("Unavailable period not found with id: " + id);
    }
}
