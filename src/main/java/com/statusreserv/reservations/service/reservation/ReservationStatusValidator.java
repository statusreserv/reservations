package com.statusreserv.reservations.service.reservation;

import com.statusreserv.reservations.model.reservation.Reservation;
import com.statusreserv.reservations.model.reservation.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Component responsible for validating allowed status transitions for reservations.
 *
 * <p>Ensures that certain state changes, such as cancellation, follow business rules.
 */
@Component
@RequiredArgsConstructor
public class ReservationStatusValidator {

    /**
     * Validates whether a reservation can be changed to the given target status.
     *
     * @param reservation   the reservation whose status is being changed
     * @param targetStatus  the desired status to change to
     *
     * @throws RuntimeException if the status change is not allowed
     */
    public void validateStatusChange(Reservation reservation, Status targetStatus) {
        switch (targetStatus) {
            case CANCELLED -> validateCancellation(reservation);
            // Future states can be added here if needed
        }
    }

    /**
     * Validates that a reservation can be cancelled.
     *
     * <p>A reservation cannot be cancelled if it is already cancelled or completed.
     *
     * @param reservation the reservation to validate
     *
     * @throws RuntimeException if the reservation cannot be cancelled
     */
    public void validateCancellation(Reservation reservation) {
        if (reservation.getStatus().equals(Status.CANCELLED)) {
            throw new RuntimeException("Reservation cannot be cancelled again");
        }

        if (reservation.getStatus().equals(Status.COMPLETED)) {
            throw new RuntimeException("Reservation cannot be cancelled after completed");
        }
    }
}
