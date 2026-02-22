package com.mourad.backend.application.service;

import com.mourad.backend.domain.exception.CarNotFoundException;
import com.mourad.backend.domain.exception.InvalidCarStateException;
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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static Car validCar() {
        return Car.create("Renault", "Clio", 2022, BigDecimal.valueOf(50), "EUR");
    }

    // ── Test 1 — createCar: happy path ────────────────────────────────────────

    @Test
    void createCar_shouldReturnCreatedCar_whenInputIsValid() {
        when(carRepository.save(any(Car.class))).thenAnswer(inv -> inv.getArgument(0));

        Car result = carService.createCar("Renault", "Clio", 2022, BigDecimal.valueOf(50), "EUR");

        assertThat(result.getBrand()).isEqualTo("Renault");
        assertThat(result.getModel()).isEqualTo("Clio");
        assertThat(result.getYear()).isEqualTo(2022);
        assertThat(result.getDailyPrice()).isEqualByComparingTo(BigDecimal.valueOf(50));
        assertThat(result.getCurrency()).isEqualTo("EUR");
        assertThat(result.getStatus()).isEqualTo(CarStatus.AVAILABLE);
        verify(carRepository).save(any(Car.class));
    }

    // ── Test 2 — createCar: domain rule — price must be > 0 ──────────────────

    @Test
    void createCar_shouldThrowInvalidCarStateException_whenDailyPriceIsNotPositive() {
        assertThatThrownBy(() ->
                carService.createCar("Renault", "Clio", 2022, BigDecimal.ZERO, "EUR"))
                .isInstanceOf(InvalidCarStateException.class)
                .hasMessageContaining("Daily price");

        verify(carRepository, never()).save(any());
    }

    // ── Test 3 — createCar: domain rule — year in [1980, currentYear+1] ───────

    @Test
    void createCar_shouldThrowInvalidCarStateException_whenYearIsOutOfRange() {
        assertThatThrownBy(() ->
                carService.createCar("Renault", "Clio", 1900, BigDecimal.valueOf(50), "EUR"))
                .isInstanceOf(InvalidCarStateException.class)
                .hasMessageContaining("year");

        verify(carRepository, never()).save(any());
    }

    // ── Test 4 — getCarById: found ────────────────────────────────────────────

    @Test
    void getCarById_shouldReturnCar_whenCarExists() {
        UUID id = UUID.randomUUID();
        Car car = validCar();
        when(carRepository.findById(id)).thenReturn(Optional.of(car));

        Car result = carService.getCarById(id);

        assertThat(result).isSameAs(car);
    }

    // ── Test 5 — getCarById: not found ────────────────────────────────────────

    @Test
    void getCarById_shouldThrowCarNotFoundException_whenCarDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(carRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> carService.getCarById(id))
                .isInstanceOf(CarNotFoundException.class)
                .hasMessageContaining(id.toString());
    }

    // ── Test 6 — getAllCars ───────────────────────────────────────────────────

    @Test
    void getAllCars_shouldReturnAllCars() {
        List<Car> cars = List.of(validCar(), validCar());
        when(carRepository.findAll()).thenReturn(cars);

        assertThat(carService.getAllCars()).hasSize(2);
        verify(carRepository).findAll();
    }

    // ── Test 7 — updateCarPrice: happy path ───────────────────────────────────

    @Test
    void updateCarPrice_shouldUpdatePriceAndCurrency_whenCarExists() {
        UUID id = UUID.randomUUID();
        Car car = validCar();
        when(carRepository.findById(id)).thenReturn(Optional.of(car));
        when(carRepository.save(any(Car.class))).thenAnswer(inv -> inv.getArgument(0));

        Car result = carService.updateCarPrice(id, BigDecimal.valueOf(75), "USD");

        assertThat(result.getDailyPrice()).isEqualByComparingTo(BigDecimal.valueOf(75));
        assertThat(result.getCurrency()).isEqualTo("USD");
        verify(carRepository).save(car);
    }

    // ── Test 8 — changeCarStatus ──────────────────────────────────────────────

    @Test
    void changeCarStatus_shouldUpdateStatus_whenCarExists() {
        UUID id = UUID.randomUUID();
        Car car = validCar();
        when(carRepository.findById(id)).thenReturn(Optional.of(car));
        when(carRepository.save(any(Car.class))).thenAnswer(inv -> inv.getArgument(0));

        Car result = carService.changeCarStatus(id, CarStatus.UNAVAILABLE);

        assertThat(result.getStatus()).isEqualTo(CarStatus.UNAVAILABLE);
        verify(carRepository).save(car);
    }
}
