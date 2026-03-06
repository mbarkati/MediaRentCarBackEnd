package com.mourad.backend.domain.model;

import com.mourad.backend.domain.exception.InvalidCarStateException;

import java.time.LocalDate;
import java.util.UUID;

public class UnavailablePeriod {

    private UUID id;
    private UUID carId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;

    private UnavailablePeriod() {}

    public static UnavailablePeriod create(UUID carId, LocalDate startDate, LocalDate endDate, String reason) {
        if (startDate == null || endDate == null) {
            throw new InvalidCarStateException("Start date and end date must not be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidCarStateException(
                    "Start date must be before or equal to end date (got: " + startDate + " > " + endDate + ")");
        }
        UnavailablePeriod period = new UnavailablePeriod();
        period.id = UUID.randomUUID();
        period.carId = carId;
        period.startDate = startDate;
        period.endDate = endDate;
        period.reason = reason;
        return period;
    }

    public static UnavailablePeriod reconstitute(UUID id, UUID carId, LocalDate startDate,
                                                  LocalDate endDate, String reason) {
        UnavailablePeriod period = new UnavailablePeriod();
        period.id = id;
        period.carId = carId;
        period.startDate = startDate;
        period.endDate = endDate;
        period.reason = reason;
        return period;
    }

    public UUID getId() { return id; }
    public UUID getCarId() { return carId; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getReason() { return reason; }
}
