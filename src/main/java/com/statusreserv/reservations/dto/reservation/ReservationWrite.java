package com.statusreserv.reservations.dto.reservation;

import com.statusreserv.reservations.model.customer.Customer;
import com.statusreserv.reservations.model.reservation.Status;
import com.statusreserv.reservations.model.user.UserAuth;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public record ReservationWrite(
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        BigDecimal totalPrice,
        Status status,
        UserAuth userAuth,
//        Customer customer,
        Set<ReservationServiceProvidedWrite> reservationService
) {}
