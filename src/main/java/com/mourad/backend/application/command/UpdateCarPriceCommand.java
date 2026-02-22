package com.mourad.backend.application.command;

import java.math.BigDecimal;

public record UpdateCarPriceCommand(
        BigDecimal dailyPrice,
        String currency
) {}
