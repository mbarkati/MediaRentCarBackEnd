package com.mourad.backend.infrastructure.config;

import com.mourad.backend.domain.model.City;
import com.mourad.backend.domain.port.out.CityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Order(3)
public class CitySeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(CitySeeder.class);

    private static final List<String> DEFAULT_CITIES = List.of(
            "Mohammadia",
            "Rabat Aéroport",
            "Rabat Centre Ville",
            "Casablanca Aéroport",
            "Casablanca Centre Ville"
    );

    private final CityRepository cityRepository;

    public CitySeeder(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!cityRepository.isEmpty()) {
            log.debug("Cities already seeded — skipping.");
            return;
        }
        DEFAULT_CITIES.forEach(name -> cityRepository.save(City.create(name)));
        log.info("Default cities seeded: {}", DEFAULT_CITIES);
    }
}
