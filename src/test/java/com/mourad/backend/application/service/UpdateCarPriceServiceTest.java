package com.mourad.backend.application.service;

import com.mourad.backend.application.command.UpdateCarPriceCommand;
import com.mourad.backend.application.dto.CarDto;
import com.mourad.backend.domain.exception.CarNotFoundException;
import com.mourad.backend.domain.model.Car;
import com.mourad.backend.domain.port.out.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCarPriceServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private UpdateCarPriceService updateCarPriceService;

    private static Car validCar() {
        return Car.create("Renault", "Clio", 2022, BigDecimal.valueOf(50), "EUR");
    }

    // ── Test 1 — happy path ───────────────────────────────────────────────────

    @Test
    void execute_shouldUpdatePrice_whenCarExists() {
        UUID id = UUID.randomUUID();
        Car car = validCar();
        when(carRepository.findById(id)).thenReturn(Optional.of(car));
        when(carRepository.save(any(Car.class))).thenAnswer(inv -> inv.getArgument(0));

        UpdateCarPriceCommand command = new UpdateCarPriceCommand(BigDecimal.valueOf(75), "USD");
        CarDto result = updateCarPriceService.execute(id, command);

        assertThat(result.dailyPrice()).isEqualByComparingTo(BigDecimal.valueOf(75));
        assertThat(result.currency()).isEqualTo("USD");
        verify(carRepository).save(car);
    }

    // ── Test 2 — car not found ────────────────────────────────────────────────

    @Test
    void execute_shouldThrowCarNotFoundException_whenCarDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(carRepository.findById(id)).thenReturn(Optional.empty());

        UpdateCarPriceCommand command = new UpdateCarPriceCommand(BigDecimal.valueOf(75), "USD");

        assertThatThrownBy(() -> updateCarPriceService.execute(id, command))
                .isInstanceOf(CarNotFoundException.class)
                .hasMessageContaining(id.toString());

        verify(carRepository, never()).save(any());
    }
}
