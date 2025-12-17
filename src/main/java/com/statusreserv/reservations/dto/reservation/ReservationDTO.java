package com.statusreserv.reservations.dto.reservation;

import com.statusreserv.reservations.model.reservation.Status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record ReservationDTO(
        UUID id,
        LocalDate date,
        LocalTime startTime,
        Status status,
        List<UUID> reservationServices
) {
}
