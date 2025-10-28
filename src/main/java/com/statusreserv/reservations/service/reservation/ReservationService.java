package com.statusreserv.reservations.service.reservation;

import com.statusreserv.reservations.dto.reservation.ReservationDTO;
import com.statusreserv.reservations.dto.reservation.ReservationWrite;
import com.statusreserv.reservations.model.reservation.Reservation;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ReservationService {

    List<ReservationDTO> findAll();

    Set<Reservation> getAll();

    ReservationDTO findReservation(UUID id);

    Reservation getById(UUID id);

    UUID create(ReservationWrite write);

    void update(UUID id, ReservationWrite write);

    void delete(UUID id);
}