package com.mourad.backend.infrastructure.persistence.adapter;

import com.mourad.backend.domain.model.Car;
import com.mourad.backend.domain.model.CarStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Persistence integration test: Flyway migrations V1-V3 + JPA mapping + CarRepositoryAdapter.
 * Uses H2 in-memory (profile "test"). Each test runs in a transaction rolled back afterwards.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Transactional
class CarRepositoryAdapterTest {

    @Autowired
    private CarRepositoryAdapter sut;

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static Car availableCar(String brand) {
        return Car.create(brand, "Model", 2022, BigDecimal.valueOf(50), "EUR", null, null, null, null, null);
    }

    // ── save ──────────────────────────────────────────────────────────────────

    @Test
    void save_shouldPersistAndReturnDomainCarWithAllFields() {
        Car car = availableCar("Renault");

        Car saved = sut.save(car);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getBrand()).isEqualTo("Renault");
        assertThat(saved.getModel()).isEqualTo("Model");
        assertThat(saved.getYear()).isEqualTo(2022);
        assertThat(saved.getDailyPrice()).isEqualByComparingTo(BigDecimal.valueOf(50));
        assertThat(saved.getCurrency()).isEqualTo("EUR");
        assertThat(saved.getStatus()).isEqualTo(CarStatus.AVAILABLE);
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    // ── findById ──────────────────────────────────────────────────────────────

    @Test
    void findById_shouldReturnCar_whenExists() {
        Car car = sut.save(availableCar("Peugeot"));

        Optional<Car> found = sut.findById(car.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getBrand()).isEqualTo("Peugeot");
        assertThat(found.get().getId()).isEqualTo(car.getId());
    }

    @Test
    void findById_shouldReturnEmpty_whenNotFound() {
        Optional<Car> found = sut.findById(UUID.randomUUID());

        assertThat(found).isEmpty();
    }

    // ── findAll ───────────────────────────────────────────────────────────────

    @Test
    void findAll_shouldReturnAllSavedCars() {
        sut.save(availableCar("Toyota"));
        sut.save(availableCar("BMW"));

        List<Car> all = sut.findAll();

        assertThat(all).hasSize(2);
        assertThat(all).extracting(Car::getBrand).containsExactlyInAnyOrder("Toyota", "BMW");
    }

    // ── findByStatus ──────────────────────────────────────────────────────────

    @Test
    void findByStatus_shouldReturnOnlyMatchingCars() {
        sut.save(availableCar("Honda"));   // AVAILABLE by default
        sut.save(availableCar("Mazda"));   // AVAILABLE by default

        assertThat(sut.findByStatus(CarStatus.AVAILABLE)).hasSize(2);
        assertThat(sut.findByStatus(CarStatus.UNAVAILABLE)).isEmpty();
    }

    // ── deleteById ────────────────────────────────────────────────────────────

    @Test
    void deleteById_shouldRemoveCarFromDatabase() {
        Car car = sut.save(availableCar("Ford"));

        sut.deleteById(car.getId());

        assertThat(sut.findById(car.getId())).isEmpty();
    }
}
