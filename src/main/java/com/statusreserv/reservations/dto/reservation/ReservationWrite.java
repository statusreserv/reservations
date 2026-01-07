package com.statusreserv.reservations.dto.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

public record ReservationWrite(
        LocalDate date,
        LocalTime startTime,
        Set<UUID> serviceProvidedIds
) {
}