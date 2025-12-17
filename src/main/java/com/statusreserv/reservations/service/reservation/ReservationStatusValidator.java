package com.statusreserv.reservations.service.reservation;

import com.statusreserv.reservations.model.reservation.Reservation;
import com.statusreserv.reservations.model.reservation.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationStatusValidator {

    public void validateStatusChange(Reservation reservation, Status targetStatus) {
        switch (targetStatus) {
            case CANCELLED -> validateCancellation(reservation);
        }
    }

    public void validateCancellation(Reservation reservation) {
        if (reservation.getStatus().equals(Status.CANCELLED)) {
            throw new RuntimeException("Reservation cannot be cancelled again");
        }

        if (reservation.getStatus().equals(Status.COMPLETED)) {
            throw new RuntimeException("Reservation cannot be cancelled after completed");
        }
    }

}