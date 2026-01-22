package com.statusreserv.reservations.service.reservation;

import com.statusreserv.reservations.constants.ValidatorType;
import com.statusreserv.reservations.model.reservation.ReservationStatus;
import com.statusreserv.reservations.service.reservation.validator.ReservationStatusValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service responsible for handling reservation confirmation requests.
 *
 * <p>This implementation coordinates the confirmation flow by:
 * <ul>
 *   <li>retrieving the reservation</li>
 *   <li>validating whether confirmation is allowed via a validator obtained from {@link ReservationStatusValidatorFactory}</li>
 *   <li>updating the reservation status to {@link ReservationStatus#CONFIRMED}</li>
 * </ul>
 *
 * <p>The {@code force} flag allows bypassing certain validation rules.
 *
 * <p>This operation runs inside a database transaction to ensure consistency
 * between validation and state update.
 */
@Service
@RequiredArgsConstructor
public class ConfirmationServiceImpl implements ConfirmationService {

    private final ReservationService reservationService;
    private final ReservationStatusValidatorFactory statusValidatorFactory;

    /**
     * Confirms the reservation identified by the given ID.
     *
     * @param id    unique identifier of the reservation to confirm
     * @param force whether the confirmation should bypass some validation rules
     * @throws RuntimeException if the reservation cannot be confirmed due to business rules
     */
    @Override
    @Transactional
    public void confirmReservation(UUID id, boolean force) {
        var reservation = reservationService.getById(id);
        statusValidatorFactory.getValidator(ValidatorType.CONFIRM).validate(reservation, force);
        reservationService.updateStatus(id, ReservationStatus.CONFIRMED);
    }
}
