package com.statusreserv.reservations.service.reservation;

import java.util.UUID;

/**
 * Handles reservation confirmation logic.
 *
 * <p>The confirmation may be performed in two modes:
 * <ul>
 *   <li>normal – business rules are strictly applied</li>
 *   <li>forced – some restrictions are bypassed</li>
 * </ul>
 */
public interface ConfirmationService {

    /**
     * Confirms the reservation identified by the given ID.
     *
     * @param id    unique identifier of the reservation
     * @param force whether the confirmation should bypass certain business rules
     */
    void confirmReservation(UUID id, boolean force);
}
