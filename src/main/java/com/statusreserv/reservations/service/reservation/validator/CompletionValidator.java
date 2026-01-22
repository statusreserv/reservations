package com.statusreserv.reservations.service.reservation.validator;

import com.statusreserv.reservations.model.reservation.Reservation;
import com.statusreserv.reservations.model.reservation.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Component responsible for validating reservation completions.
 *
 * <p>This validator checks whether a reservation can be marked as completed according to
 * business rules and tenant-specific configurations. It supports both normal
 * and forced completions.
 *
 * <p>Validation rules include:
 * <ul>
 *     <li>Status change validation via {@link ReservationStatusChangeValidator}</li>
 *     <li>Reservation end date/time validation via {@link ReservationTimingValidator}</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class CompletionValidator implements ReservationStatusValidator {

    private final ReservationStatusChangeValidator statusValidator;
    private final ReservationTimingValidator timingValidator;

    /**
     * Validates if a reservation can be marked as completed.
     *
     * <p>If {@code force} is true, only basic status change validation is applied.
     * Otherwise, all normal completion rules are enforced.
     *
     * @param reservation the reservation to validate
     * @param force       whether to force the completion (bypass some rules)
     * @throws RuntimeException if the completion is not allowed
     */
    @Override
    public void validate(Reservation reservation, boolean force) {
        if (force) {
            validateForceCompletion(reservation);
        } else {
            validateNormalCompletion(reservation);
        }
    }

    /**
     * Performs normal completion validations.
     *
     * <p>Checks that the reservation status can be changed to {@link ReservationStatus#COMPLETED}
     * and that the reservation has already ended.
     *
     * @param reservation the reservation to validate
     * @throws RuntimeException if the completion is not allowed
     */
    private void validateNormalCompletion(Reservation reservation) {
        statusValidator.validateStatusChange(reservation, ReservationStatus.COMPLETED);
        timingValidator.validateReservationAlreadyEnded(reservation);
    }

    /**
     * Performs forced completion validation.
     *
     * <p>Only validates that the status can be changed to {@link ReservationStatus#COMPLETED},
     * ignoring other business rules.
     *
     * @param reservation the reservation to validate
     * @throws RuntimeException if the status change is not allowed
     */
    private void validateForceCompletion(Reservation reservation) {
        statusValidator.validateStatusChange(reservation, ReservationStatus.COMPLETED);
    }
}
