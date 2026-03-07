package com.mourad.backend.application.command;

import com.mourad.backend.domain.model.Fuel;
import com.mourad.backend.domain.model.Transmission;

import java.math.BigDecimal;

public record UpdateCarCommand(
        String brand,
        String model,
        int year,
        BigDecimal dailyPrice,
        String currency,
        Integer seats,
        Transmission transmission,
        Fuel fuel,
        String city,
        String imageUrl
) {}
