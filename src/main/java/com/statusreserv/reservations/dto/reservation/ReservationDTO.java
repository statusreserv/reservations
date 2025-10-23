package com.statusreserv.reservations.dto.reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

public record ReservationDTO(
        UUID id,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        BigDecimal totalPrice,
        Set<ReservationServiceProvidedDTO> reservationService
) {}
