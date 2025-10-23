package com.statusreserv.reservations.dto.reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public record ReservationWrite(
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        BigDecimal totalPrice,
        Set<ReservationServiceProvidedWrite> reservationService
) {}
