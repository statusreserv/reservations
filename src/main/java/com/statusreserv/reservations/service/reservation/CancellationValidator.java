package com.statusreserv.reservations.service.reservation;

import com.statusreserv.reservations.model.reservation.Reservation;
import com.statusreserv.reservations.model.reservation.ReservationStatus;
import com.statusreserv.reservations.model.tenant.TenantConfigType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Component responsible for validating reservation cancellations.
 *
 * <p>This validator checks whether a reservation can be cancelled according to
 * business rules and tenant-specific configurations. It supports both normal
 * and forced cancellations.
 *
 * <p>Validation rules include:
 * <ul>
 *     <li>Status change validation via {@link ReservationStatusValidator}</li>
 *     <li>Reservation start date/time validation</li>
 *     <li>Minimum days before cancellation rules based on tenant configuration</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class CancellationValidator {

    private final ReservationStatusValidator validator;

    /**
     * Validates if a reservation can be cancelled.
     *
     * <p>If {@code force} is true, only basic status change validation is applied.
     * Otherwise, all normal cancellation rules are enforced.
     *
     * @param reservation the reservation to validate
     * @param force       whether to force the cancellation (bypass some rules)
     *
     * @throws RuntimeException if the cancellation is not allowed
     */
    public void validateCancellation(Reservation reservation, boolean force) {
        if (force) {
            validateForceCancellation(reservation);
        } else {
            validateNormalCancellation(reservation);
        }
    }

    /**
     * Performs normal cancellation validations.
     *
     * <p>Checks status change, reservation not started, and minimum days before cancellation.
     *
     * @param reservation the reservation to validate
     *
     * @throws RuntimeException if the cancellation is not allowed
     */
    private void validateNormalCancellation(Reservation reservation) {
        validator.validateStatusChange(reservation, ReservationStatus.CANCELLED);

        if (reservation.getStatus().equals(ReservationStatus.CONFIRMED)) {
            validateReservationNotStarted(reservation);
            validateMinDaysBeforeCancellation(reservation);
        }

        if (reservation.getStatus().equals(ReservationStatus.PENDING)) {
            validateReservationNotStarted(reservation);
        }
    }

    /**
     * Performs forced cancellation validation.
     *
     * <p>Only validates that the status can be changed to {@link ReservationStatus#CANCELLED}.
     *
     * @param reservation the reservation to validate
     *
     * @throws RuntimeException if the status change is not allowed
     */
    private void validateForceCancellation(Reservation reservation) {
        validator.validateStatusChange(reservation, ReservationStatus.CANCELLED);
    }

    /**
     * Validates that the reservation has not started yet.
     *
     * @param reservation the reservation to validate
     *
     * @throws RuntimeException if the reservation has already started
     */
    private void validateReservationNotStarted(Reservation reservation) {
        LocalDateTime reservationDateTime = reservation.getDate().atTime(reservation.getStartTime());
        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("The reservation has already started.");
        }
    }

    /**
     * Validates if the cancellation meets the minimum days requirement defined by the tenant.
     *
     * @param reservation the reservation to validate
     *
     * @throws RuntimeException if the cancellation is attempted too close to the reservation date
     */
    private void validateMinDaysBeforeCancellation(Reservation reservation) {
        Integer minDaysBeforeCancellation = reservation.getTenant().getConfigSet()
                .stream()
                .filter(config -> config.getProperty().equals(TenantConfigType.MIN_DAYS_BEFORE_CANCELLATION))
                .findFirst()
                .map(config -> Integer.parseInt(config.getValue()))
                .orElse(null);

        if (minDaysBeforeCancellation != null) {
            LocalDateTime earliestCancellationDate = reservation.getDate().atStartOfDay()
                    .minusDays(minDaysBeforeCancellation);

            if (LocalDateTime.now().isAfter(earliestCancellationDate)) {
                throw new RuntimeException(
                        "Reservation cannot be cancelled. Cancellations are allowed only "
                                + minDaysBeforeCancellation + " days prior to the reservation date."
                );
            }
        }
    }
}
