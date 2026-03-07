package com.mourad.backend.application.service;

import com.mourad.backend.application.dto.CarDto;
import com.mourad.backend.domain.model.Car;
import com.mourad.backend.domain.model.CarStatus;
import com.mourad.backend.domain.port.out.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCarsServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private GetCarsService getCarsService;

    private static Car validCar() {
        return Car.create("Renault", "Clio", 2022, BigDecimal.valueOf(50), "EUR", null, null, null, null, null);
    }

    // ── Test 1 — findAll ──────────────────────────────────────────────────────

    @Test
    void findAll_shouldReturnAllCarsAsDtos() {
        List<Car> cars = List.of(validCar(), validCar());
        when(carRepository.findAll()).thenReturn(cars);

        List<CarDto> result = getCarsService.findAll();

        assertThat(result).hasSize(2);
        verify(carRepository).findAll();
    }

    // ── Test 2 — findByStatus ─────────────────────────────────────────────────

    @Test
    void findByStatus_shouldReturnFilteredCarsAsDtos() {
        List<Car> cars = List.of(validCar());
        when(carRepository.findByStatus(CarStatus.AVAILABLE)).thenReturn(cars);

        List<CarDto> result = getCarsService.findByStatus(CarStatus.AVAILABLE);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).status()).isEqualTo(CarStatus.AVAILABLE);
        verify(carRepository).findByStatus(CarStatus.AVAILABLE);
    }
}
