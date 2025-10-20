package com.statusreserv.reservations.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ServiceProvidedDTO(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        int durationMinutes
) {}
