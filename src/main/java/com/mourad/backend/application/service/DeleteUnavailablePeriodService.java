package com.mourad.backend.application.service;

import com.mourad.backend.application.port.in.DeleteUnavailablePeriodUseCase;
import com.mourad.backend.domain.exception.UnavailablePeriodNotFoundException;
import com.mourad.backend.domain.port.out.UnavailablePeriodRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class DeleteUnavailablePeriodService implements DeleteUnavailablePeriodUseCase {

    private final UnavailablePeriodRepository periodRepository;

    public DeleteUnavailablePeriodService(UnavailablePeriodRepository periodRepository) {
        this.periodRepository = periodRepository;
    }

    @Override
    public void execute(UUID periodId) {
        periodRepository.findById(periodId)
                .orElseThrow(() -> new UnavailablePeriodNotFoundException(periodId));
        periodRepository.deleteById(periodId);
    }
}
