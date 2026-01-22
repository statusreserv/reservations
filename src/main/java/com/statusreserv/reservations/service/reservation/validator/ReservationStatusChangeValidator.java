package com.statusreserv.reservations.service.reservation.validator;

import com.statusreserv.reservations.model.reservation.Reservation;
import com.statusreserv.reservations.model.reservation.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Component responsible for validating allowed status transitions for reservations.
 *
 * <p>Ensures that certain state changes, such as cancellation, follow business rules.
 */
@Component
@RequiredArgsConstructor
public class ReservationStatusChangeValidator {

    /**
     * Validates whether a reservation can be changed to the given target status.
     *
     * @param reservation   the reservation whose status is being changed
     * @param targetStatus  the desired status to change to
     *
     * @throws RuntimeException if the status change is not allowed
     */
    public void validateStatusChange(Reservation reservation, ReservationStatus targetStatus) {
        switch (targetStatus) {
            case CANCELLED -> validateCancellation(reservation);
            case CONFIRMED -> validateConfirmation(reservation);
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
    private void validateCancellation(Reservation reservation) {
        if (reservation.getStatus().equals(ReservationStatus.CANCELLED)) {
            throw new RuntimeException("Reservation cannot be cancelled again");
        }

        if (reservation.getStatus().equals(ReservationStatus.EXPIRED)) {
            throw new RuntimeException("Reservation cannot be cancelled after expired");
        }

        if (reservation.getStatus().equals(ReservationStatus.COMPLETED)) {
            throw new RuntimeException("Reservation cannot be cancelled after completed");
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
    private void validateConfirmation(Reservation reservation) {
        if (reservation.getStatus().equals(ReservationStatus.CANCELLED)) {
            throw new RuntimeException("Reservation cannot be confirmed after cancelled");
        }

        if (reservation.getStatus().equals(ReservationStatus.EXPIRED)) {
            throw new RuntimeException("Reservation cannot be confirmed after expired");
        }

        if (reservation.getStatus().equals(ReservationStatus.COMPLETED)) {
            throw new RuntimeException("Reservation cannot be confirmed after completed");
        }

        if (reservation.getStatus().equals(ReservationStatus.CONFIRMED)) {
            throw new RuntimeException("Reservation cannot be confirmed again");
        }
    }
}
