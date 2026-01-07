package com.statusreserv.reservations.service.reservation;

import java.util.UUID;

/**
 * Handles reservation completion logic.
 *
 * <p>The completion may be performed in two modes:
 * <ul>
 *   <li>normal – business rules are strictly applied</li>
 *   <li>forced – some restrictions are bypassed</li>
 * </ul>
 *
 * <p>This service is responsible for marking reservations as completed, ensuring
 * that all relevant rules (such as end time validation and status changes) are respected.
 */
public interface CompletionService {

    /**
     * Marks the reservation identified by the given ID as completed.
     *
     * <p>If {@code force} is true, some business rules may be bypassed.
     * Otherwise, all normal completion rules are enforced.
     *
     * @param id    unique identifier of the reservation
     * @param force whether the completion should bypass certain business rules
     */
    void completeReservation(UUID id, boolean force);
}
