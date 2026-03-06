package com.mourad.backend.domain.port.out;

import com.mourad.backend.domain.model.UnavailablePeriod;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UnavailablePeriodRepository {

    UnavailablePeriod save(UnavailablePeriod period);

    List<UnavailablePeriod> findByCarId(UUID carId);

    Optional<UnavailablePeriod> findById(UUID id);

    void deleteById(UUID id);
}
