package com.mourad.backend.application.command;

import java.time.LocalDate;

public record CreateUnavailablePeriodCommand(
        LocalDate startDate,
        LocalDate endDate,
        String reason
) {}
