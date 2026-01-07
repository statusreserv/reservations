package com.statusreserv.reservations.service.reservation;

import com.statusreserv.reservations.constants.ValidatorType;
import com.statusreserv.reservations.model.reservation.ReservationStatus;
import com.statusreserv.reservations.service.reservation.validator.ReservationStatusValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service responsible for handling reservation completion requests.
 *
 * <p>This implementation coordinates the completion flow by:
 * <ul>
 *   <li>retrieving the reservation</li>
 *   <li>validating whether completion is allowed via a validator obtained from {@link ReservationStatusValidatorFactory}</li>
 *   <li>updating the reservation status to {@link ReservationStatus#COMPLETED}</li>
 * </ul>
 *
 * <p>The {@code force} flag allows bypassing certain validation rules.
 *
 * <p>This operation runs inside a database transaction to ensure consistency
 * between validation and state update.
 */
@Service
@RequiredArgsConstructor
public class CompletionServiceImpl implements CompletionService {

    private final ReservationService reservationService;
    private final ReservationStatusValidatorFactory statusValidatorFactory;

    /**
     * Marks the reservation identified by the given ID as completed.
     *
     * <p>If {@code force} is true, some business rules may be bypassed.
     * Otherwise, all normal completion rules are enforced.
     *
     * @param id    unique identifier of the reservation to complete
     * @param force whether the completion should bypass some validation rules
     * @throws RuntimeException if the reservation cannot be completed due to business rules
     */
    @Override
    @Transactional
    public void completeReservation(UUID id, boolean force) {
        var reservation = reservationService.getById(id);
        statusValidatorFactory.getValidator(ValidatorType.COMPLETE).validate(reservation, force);
        reservationService.updateStatus(id, ReservationStatus.COMPLETED);
    }
}
