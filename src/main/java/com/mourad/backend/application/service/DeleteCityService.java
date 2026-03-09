package com.mourad.backend.application.service;

import com.mourad.backend.application.port.in.DeleteCityUseCase;
import com.mourad.backend.domain.exception.CityNotFoundException;
import com.mourad.backend.domain.port.out.CityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class DeleteCityService implements DeleteCityUseCase {

    private final CityRepository cityRepository;

    public DeleteCityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public void execute(UUID id) {
        if (!cityRepository.existsById(id)) {
            throw new CityNotFoundException(id);
        }
        cityRepository.deleteById(id);
    }
}
