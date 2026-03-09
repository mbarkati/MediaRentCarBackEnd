package com.mourad.backend.application.service;

import com.mourad.backend.application.command.CreateCityCommand;
import com.mourad.backend.application.dto.CityDto;
import com.mourad.backend.application.port.in.CreateCityUseCase;
import com.mourad.backend.domain.model.City;
import com.mourad.backend.domain.port.out.CityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateCityService implements CreateCityUseCase {

    private final CityRepository cityRepository;

    public CreateCityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public CityDto execute(CreateCityCommand command) {
        City city = City.create(command.name());
        return CityDto.from(cityRepository.save(city));
    }
}
