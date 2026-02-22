package com.mourad.backend.application.port.in;

import java.util.UUID;

public interface DeleteCarUseCase {
    void execute(UUID id);
}
