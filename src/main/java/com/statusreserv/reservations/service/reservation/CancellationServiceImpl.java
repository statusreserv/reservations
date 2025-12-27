package com.statusreserv.reservations.service.reservation;

import com.statusreserv.reservations.model.reservation.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service responsible for handling reservation cancellation requests.
 *
 * <p>This implementation coordinates the cancellation flow by:
 * <ul>
 *   <li>retrieving the reservation</li>
 *   <li>validating whether cancellation is allowed</li>
 *   <li>updating the reservation status to {@link Status#CANCELLED}</li>
 * </ul>
 *
 * <p>Business validation rules are delegated to {@link CancellationValidator}.
 */
@Service
@RequiredArgsConstructor
public class CancellationServiceImpl implements CancellationService {

    private final ReservationService reservationService;
    private final CancellationValidator cancellationValidator;

    /**
     * Cancels the reservation identified by the given ID.
     *
     * <p>Depending on the {@code force} flag, the cancellation may ignore
     * certain business restrictions.
     *
     * <p>This operation runs inside a database transaction to ensure
     * consistency between validation and state update.
     *
     * @param id    unique identifier of the reservation to cancel
     * @param force whether the cancellation should bypass some validation rules
     *
     */
    @Override
    @Transactional
    public void cancelReservation(UUID id, boolean force) {

        var reservation = reservationService.getById(id);

        cancellationValidator.validateCancellation(reservation, force);

        reservationService.updateStatus(id, Status.CANCELLED);
    }
}
