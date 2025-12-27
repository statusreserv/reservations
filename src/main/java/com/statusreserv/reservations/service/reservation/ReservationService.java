package com.statusreserv.reservations.service.reservation;

import com.statusreserv.reservations.dto.reservation.ReservationDTO;
import com.statusreserv.reservations.dto.reservation.ReservationWrite;
import com.statusreserv.reservations.model.reservation.Reservation;
import com.statusreserv.reservations.model.reservation.ReservationStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Service responsible for managing reservations.
 */
@Service
public interface ReservationService {

    /**
     * Retrieves all reservations as DTOs.
     *
     * @return List of ReservationDTO
     */
    List<ReservationDTO> findAll();

    /**
     * Retrieves all reservations as entities.
     *
     * @return Set of Reservation entities
     */
    Set<Reservation> getAll();

    /**
     * Finds a reservation by its UUID.
     *
     * @param id UUID of the reservation
     * @return ReservationDTO corresponding to the given ID
     */
    ReservationDTO findReservation(UUID id);

    /**
     * Retrieves a reservation entity by its UUID.
     *
     * @param id UUID of the reservation
     * @return Reservation entity
     */
    Reservation getById(UUID id);

    /**
     * Creates a new reservation.
     *
     * @param write ReservationWrite DTO containing reservation data
     * @return UUID of the newly created reservation
     */
    UUID create(ReservationWrite write);

    /**
     * Updates the status of an existing reservation.
     *
     * @param id UUID of the reservation
     * @param status New status to set
     */
    void updateStatus(UUID id, ReservationStatus status);
}
