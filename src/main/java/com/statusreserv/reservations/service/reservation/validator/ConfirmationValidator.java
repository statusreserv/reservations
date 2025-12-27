package com.statusreserv.reservations.service.reservation.validator;

import com.statusreserv.reservations.model.reservation.Reservation;
import com.statusreserv.reservations.model.reservation.ReservationStatus;
import com.statusreserv.reservations.model.tenant.TenantConfigType;
import com.statusreserv.reservations.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Component responsible for validating reservation confirmations.
 *
 * <p>This validator checks whether a reservation can be confirmed according to
 * business rules and tenant-specific configurations. It supports both normal
 * and forced confirmations.
 *
 * <p>Validation rules include:
 * <ul>
 *     <li>Status change validation via {@link ReservationStatusChangeValidator}</li>
 *     <li>Reservation start date/time validation via {@link ReservationTimingValidator}</li>
 *     <li>Overlap checks with existing reservations via {@link ReservationValidator}</li>
 *     <li>Minimum days before confirmation rules based on tenant configuration</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class ConfirmationValidator implements ReservationStatusValidator {

    private final ReservationStatusChangeValidator statusValidator;
    private final ReservationTimingValidator timingValidator;
    private final ReservationValidator reservationValidator;
    private final ReservationRepository reservationRepository;

    /**
     * Validates if a reservation can be confirmed.
     *
     * <p>If {@code force} is true, only basic status change validation is applied.
     * Otherwise, all normal confirmation rules are enforced.
     *
     * @param reservation the reservation to validate
     * @param force       whether to force the confirmation (bypass some rules)
     * @throws RuntimeException if the confirmation is not allowed
     */
    @Override
    public void validate(Reservation reservation, boolean force) {
        if (force) {
            validateForceConfirmation(reservation);
        } else {
            validateNormalConfirmation(reservation);
        }
    }

    /**
     * Performs normal confirmation validations.
     *
     * <p>Checks status change, reservation not started, minimum days before confirmation,
     * and overlapping reservations.
     *
     * @param reservation the reservation to validate
     * @throws RuntimeException if the confirmation is not allowed
     */
    private void validateNormalConfirmation(Reservation reservation) {
        statusValidator.validateStatusChange(reservation, ReservationStatus.CONFIRMED);
        timingValidator.validateReservationNotStarted(reservation);

        if (reservation.getStatus().equals(ReservationStatus.PENDING)) {
            timingValidator.validateMinDaysBefore(reservation, TenantConfigType.MIN_DAYS_BEFORE_CONFIRMATION);
            validateOverlaps(reservation);
        }
    }

    /**
     * Checks if the given reservation overlaps with other reservations of the same tenant on the same date.
     *
     * <p>Fetches all reservations for the tenant on the reservation's date, excluding the reservation itself,
     * and delegates the overlap validation to {@link ReservationValidator#checkOverlap}.
     *
     * @param reservation the reservation to validate for overlaps
     * @throws RuntimeException if an overlapping reservation is detected
     */
    private void validateOverlaps(Reservation reservation) {
        var existingReservations = reservationRepository.findByTenantIdAndDate(
                        reservation.getTenant().getId(), reservation.getDate()
                ).stream()
                .filter(r -> !r.getId().equals(reservation.getId()))
                .toList();

        reservationValidator.checkOverlap(existingReservations, reservation);
    }


    /**
     * Performs forced confirmation validation.
     *
     * <p>Only validates that the status can be changed to {@link ReservationStatus#CONFIRMED}.
     *
     * @param reservation the reservation to validate
     * @throws RuntimeException if the status change is not allowed
     */
    private void validateForceConfirmation(Reservation reservation) {
        statusValidator.validateStatusChange(reservation, ReservationStatus.CONFIRMED);
    }
}
