package com.mourad.backend.application.port.in;

import java.util.UUID;

public interface DeleteCityUseCase {
    void execute(UUID id);
}
