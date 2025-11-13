package com.statusreserv.reservations.model.email;

import com.statusreserv.reservations.model.reservation.Reservation;
import lombok.Getter;

@Getter
public class EmailReservationCreatedRequest extends EmailRequest {

    private final Reservation reservation;

    public EmailReservationCreatedRequest(String from, String to, Reservation reservation) {
        super(from, to);
        this.reservation = reservation;
    }
}