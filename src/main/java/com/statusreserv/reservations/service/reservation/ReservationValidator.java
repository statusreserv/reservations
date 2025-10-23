package com.statusreserv.reservations.service.reservation;

import com.statusreserv.reservations.model.reservation.Reservation;
import com.statusreserv.reservations.model.reservation.ReservationServiceProvided;
import com.statusreserv.reservations.model.tenant.Tenant;
import com.statusreserv.reservations.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReservationValidator {

    private final ReservationRepository repository;

    public void validateReservation(Reservation reservation, UUID ignoreId) {
        Tenant tenant = reservation.getTenant();
        LocalDate date = reservation.getDate();
        Set<ReservationServiceProvided> reservationService = reservation.getServiceReservation();

        reservationService.forEach(this::validateReservation);

        repository.findByTenantIdAndDate(tenant.getId(), date)
                .stream()
                .filter(existing -> !existing.getId().equals(ignoreId))
                .forEach(existing -> {
                    for (ReservationServiceProvided existingReservation : existing.getServiceReservation()) {
                        for (ReservationServiceProvided newReservation : reservationService) {
                            if (isOverlapping(existingReservation, newReservation)) {
                                throw new IllegalArgumentException("Reservation already exists" + existingReservation.getId() + " on " + existing.getDate());
                            }
                        }
                    }
                });
    }

    private void validateReservation(ReservationServiceProvided reservation) {
        if (reservation.getReservation().getDate() == null ||
                reservation.getReservation().getStartTime() == null ||
                reservation.getReservation().getEndTime() == null ||
                reservation.getReservation().getTotalPrice() == null) {
            throw new IllegalArgumentException("Reservation must include date, start time, end time, and total price");
        }

        if (!reservation.getReservation().getStartTime().isBefore(reservation.getReservation().getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

    }

    private boolean isOverlapping(ReservationServiceProvided a, ReservationServiceProvided b) {
        LocalTime startA = a.getReservation().getStartTime();
        LocalTime endA = a.getReservation().getEndTime();
        LocalTime startB = b.getReservation().getStartTime();
        LocalTime endB = b.getReservation().getEndTime();

        return startA.isBefore(endB) && startB.isBefore(endA);
    }

}
