package com.mourad.backend.domain.port.out;

import com.mourad.backend.domain.model.City;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CityRepository {

    City save(City city);

    List<City> findAll();

    Optional<City> findById(UUID id);

    boolean existsById(UUID id);

    boolean isEmpty();

    void deleteById(UUID id);
}
