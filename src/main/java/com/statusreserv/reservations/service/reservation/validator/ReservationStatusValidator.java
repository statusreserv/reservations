package com.statusreserv.reservations.service.reservation.validator;

import com.statusreserv.reservations.model.reservation.Reservation;

/**
 * Contract for validating reservation status transitions.
 *
 * <p>Implementations of this interface encapsulate the business rules
 * that determine whether a reservation is allowed to change to a given
 * status (e.g. confirmation, cancellation, etc.).</p>
 *
 * <p>Depending on the business context and tenant configuration, different
 * validation strategies may be applied. The {@code force} parameter allows
 * bypassing some non-critical rules while still enforcing mandatory ones
 * (such as basic state integrity).</p>
 */
public interface ReservationStatusValidator {

    /**
     * Validates whether a reservation can transition to the target status.
     *
     * <p>If {@code force} is {@code true}, only mandatory rules are enforced.
     * If {@code false}, all business rules are applied.</p>
     *
     * @param reservation the reservation being validated
     * @param force       whether the validation should bypass optional rules
     * @throws RuntimeException if the transition is not allowed
     */
    void validate(Reservation reservation, boolean force);
}
