package com.mourad.backend.application.service;

import com.mourad.backend.application.dto.UnavailablePeriodDto;
import com.mourad.backend.application.port.in.GetUnavailablePeriodsUseCase;
import com.mourad.backend.domain.port.out.UnavailablePeriodRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class GetUnavailablePeriodsService implements GetUnavailablePeriodsUseCase {

    private final UnavailablePeriodRepository periodRepository;

    public GetUnavailablePeriodsService(UnavailablePeriodRepository periodRepository) {
        this.periodRepository = periodRepository;
    }

    @Override
    public List<UnavailablePeriodDto> findByCarId(UUID carId) {
        return periodRepository.findByCarId(carId).stream()
                .map(UnavailablePeriodDto::from)
                .collect(Collectors.toList());
    }
}
