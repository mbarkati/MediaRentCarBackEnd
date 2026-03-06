package com.mourad.backend.application.port.in;

import java.util.UUID;

public interface DeleteUnavailablePeriodUseCase {
    void execute(UUID periodId);
}
