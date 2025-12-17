package com.statusreserv.reservations.model.email;

import com.statusreserv.reservations.model.reservation.Reservation;
import lombok.Getter;

@Getter
public class EmailReservationTemplateCreatedRequest extends EmailTemplateRequest {

    private final Reservation reservation;

    public EmailReservationTemplateCreatedRequest(String from, String to, Reservation reservation) {
        super(from, to);
        this.reservation = reservation;
    }
}