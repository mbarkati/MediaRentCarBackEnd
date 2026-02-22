package com.mourad.backend.application.command;

import java.math.BigDecimal;

public record CreateCarCommand(
        String brand,
        String model,
        int year,
        BigDecimal dailyPrice,
        String currency
) {}
