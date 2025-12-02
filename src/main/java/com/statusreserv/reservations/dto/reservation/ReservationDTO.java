package com.statusreserv.reservations.dto.reservation;

import com.statusreserv.reservations.model.reservation.Status;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record ReservationDTO(
        UUID id,
        @NotNull(message = "Reservation must have a date")
        LocalDate date,
        @NotNull(message = "Reservation must have a start time")
        LocalTime startTime,
        Status status,
        List<UUID> reservationServices
) {
}
