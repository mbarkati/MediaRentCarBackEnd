package com.mourad.backend.application.service;

import com.mourad.backend.application.dto.CityDto;
import com.mourad.backend.application.port.in.GetCitiesUseCase;
import com.mourad.backend.domain.port.out.CityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GetCitiesService implements GetCitiesUseCase {

    private final CityRepository cityRepository;

    public GetCitiesService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public List<CityDto> findAll() {
        return cityRepository.findAll().stream()
                .map(CityDto::from)
                .toList();
    }
}
