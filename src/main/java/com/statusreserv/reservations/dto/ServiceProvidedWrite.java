package com.statusreserv.reservations.dto;

import java.math.BigDecimal;

public record ServiceProvidedWrite(
        String name,
        String description,
        BigDecimal price,
        int durationMinutes
) {}
