package com.statusreserv.reservations.dto.reservation;

import java.math.BigDecimal;
import java.util.UUID;

public record ReservationServiceProvidedWrite(
        String name,
        String description,
        BigDecimal price,
        int durationMinutes,
        UUID serviceProvidedId
) {
}
