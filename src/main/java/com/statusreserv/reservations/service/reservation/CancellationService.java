package com.statusreserv.reservations.service.reservation;

import java.util.UUID;

/**
 * Handles reservation cancellation logic.
 *
 * <p>The cancellation may be performed in two modes:
 * <ul>
 *   <li>normal – business rules are strictly applied</li>
 *   <li>forced – some restrictions are bypassed</li>
 * </ul>
 */
public interface CancellationService {

    /**
     * Cancels the reservation identified by the given ID.
     *
     * @param id    unique identifier of the reservation
     * @param force whether the cancellation should bypass certain business rules
     */
    void cancelReservation(UUID id, boolean force);
}
