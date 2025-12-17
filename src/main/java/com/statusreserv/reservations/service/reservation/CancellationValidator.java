package com.statusreserv.reservations.service.reservation;

import com.statusreserv.reservations.model.reservation.Reservation;
import com.statusreserv.reservations.model.reservation.Status;
import com.statusreserv.reservations.model.tenant.TenantConfigType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CancellationValidator {

    private final ReservationStatusValidator validator;

    public void validateCancellation(Reservation reservation) {
        validator.validateStatusChange(reservation, Status.CANCELLED);

        if (reservation.getStatus().equals(Status.CONFIRMED)) {
            validateReservationNotStarted(reservation);
            validateMinDaysBeforeCancellation(reservation);
        }

        if (reservation.getStatus().equals(Status.PENDING)) {
            validateReservationNotStarted(reservation);
        }
    }

    private void validateReservationNotStarted(Reservation reservation) {
        LocalDateTime reservationDateTime = reservation.getDate().atTime(reservation.getStartTime());
        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("The reservation has already started.");
        }
    }

    private void validateMinDaysBeforeCancellation(Reservation reservation) {
        Integer minDaysBeforeCancellation = reservation.getTenant().getConfigSet()
                .stream()
                .filter(config -> config.getProperty().equals(TenantConfigType.MIN_DAYS_BEFORE_CANCELLATION))
                .findFirst()
                .map(config -> Integer.parseInt(config.getValue()))
                .orElse(null);

        if (minDaysBeforeCancellation != null) {
            LocalDateTime earliestCancellationDate = reservation.getDate().atStartOfDay()
                    .minusDays(minDaysBeforeCancellation);

            if (LocalDateTime.now().isAfter(earliestCancellationDate)) {
                throw new RuntimeException(
                        "Reservation cannot be cancelled. Cancellations are allowed only "
                                + minDaysBeforeCancellation + " days prior to the reservation date."
                );
            }
        }
    }
}
