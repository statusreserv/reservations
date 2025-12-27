package com.statusreserv.reservations.service.reservation.validator;

import com.statusreserv.reservations.model.reservation.Reservation;
import com.statusreserv.reservations.model.tenant.TenantConfigType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Responsible for validating reservation date rules.
 *
 * <p>This component applies business constraints related to
 * reservation timing.</p>
 *
 * <p>Examples include:</p>
 * <ul>
 *     <li>validating whether the reservation has already started</li>
 *     <li>validating deadlines configured by the tenant</li>
 * </ul>
 */
@Component
public class ReservationTimingValidator {

    /**
     * Validates that the reservation has not already started.
     *
     * <p>The validation compares the current timestamp with the reservation start.</p>
     *
     * @param reservation the reservation being validated
     *
     * @throws RuntimeException if the reservation has already started
     */
    public void validateReservationNotStarted(Reservation reservation) {
        LocalDateTime reservationDateTime =
                reservation.getDate().atTime(reservation.getStartTime());

        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("The reservation has already started.");
        }
    }

    /**
     * Validates the minimum number of days required before performing the action.
     *
     * <p>The number of days is defined at tenant configuration level
     * and resolved via {@link TenantConfigType}.</p>
     *
     * <p>If no configuration exists, this validation is skipped.</p>
     *
     * @param reservation the reservation being validated
     * @param tenantConfigType the configuration property to evaluate
     *
     * @throws RuntimeException if the operation is attempted too close to the reservation date
     */
    public void validateMinDaysBefore(Reservation reservation,
                                       TenantConfigType tenantConfigType) {

        Integer minDaysBefore = reservation.getTenant().getConfigSet()
                .stream()
                .filter(config -> config.getProperty().equals(tenantConfigType))
                .findFirst()
                .map(config -> Integer.parseInt(config.getValue()))
                .orElse(null);

        if (minDaysBefore != null) {
            LocalDateTime earliestAllowedDate =
                    reservation.getDate().atStartOfDay()
                            .minusDays(minDaysBefore);

            if (LocalDateTime.now().isAfter(earliestAllowedDate)) {
                throw new RuntimeException(
                        "Operation not allowed. Minimum advance time is "
                                + minDaysBefore + " days."
                );
            }
        }
    }
}
