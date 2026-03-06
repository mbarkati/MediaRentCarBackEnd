package com.mourad.backend.application.dto;

import com.mourad.backend.domain.model.UnavailablePeriod;

import java.time.LocalDate;
import java.util.UUID;

public record UnavailablePeriodDto(
        UUID id,
        LocalDate startDate,
        LocalDate endDate,
        String reason
) {
    public static UnavailablePeriodDto from(UnavailablePeriod period) {
        return new UnavailablePeriodDto(
                period.getId(),
                period.getStartDate(),
                period.getEndDate(),
                period.getReason()
        );
    }
}
