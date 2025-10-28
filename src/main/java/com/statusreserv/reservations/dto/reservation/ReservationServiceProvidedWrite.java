package com.statusreserv.reservations.dto.reservation;

import java.math.BigDecimal;

public record ReservationServiceProvidedWrite(
        String name,
        String description,
        BigDecimal price,
        int durationMinutes
) {}
