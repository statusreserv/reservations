package com.statusreserv.reservations.service.reservation;

import com.statusreserv.reservations.model.reservation.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CancellationServiceImpl implements CancellationService {

    private final ReservationService reservationService;
    private final CancellationValidator cancellationValidator;

    @Override
    public void cancelReservation(UUID id) {
        var reservation = reservationService.getById(id);
        cancellationValidator.validateCancellation(reservation);
        reservationService.updateStatus(id, Status.CANCELLED);
    }
}
