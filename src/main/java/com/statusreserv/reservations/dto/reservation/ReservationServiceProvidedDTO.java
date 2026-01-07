package com.statusreserv.reservations.dto.reservation;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record ReservationServiceProvidedDTO(
        @NotNull(message = "You need to choose a service")
        UUID serviceProvidedId,
        @NotNull(message = "Reservation should have a name")
        String name,
        @NotNull(message = "Reservation should have a description")
        String description,
        @NotNull(message = "Reservation should have a price")
        @Digits(integer = 4, fraction = 2)
        BigDecimal price,
        @NotNull(message = "Reservation should have a duration")
        int durationMinutes
) {
}
