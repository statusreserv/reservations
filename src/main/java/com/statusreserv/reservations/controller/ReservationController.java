package com.statusreserv.reservations.controller;

import com.statusreserv.reservations.dto.reservation.ReservationDTO;
import com.statusreserv.reservations.dto.reservation.ReservationWrite;
import com.statusreserv.reservations.service.reservation.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Endpoints for managing reservations")
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * Retrieves all reservations.
     *
     * @return List of ReservationDTO
     */
    @GetMapping
    @Operation(summary = "Get all reservations", description = "Retrieve a list of all reservations")
    public ResponseEntity<List<ReservationDTO>> getAll() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    /**
     * Retrieves a reservation by its ID.
     *
     * @param id UUID of the reservation
     * @return ReservationDTO corresponding to the given ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get reservation by ID", description = "Retrieve a single reservation by its unique ID")
    public ResponseEntity<ReservationDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(reservationService.findReservation(id));
    }

    /**
     * Creates a new reservation.
     *
     * @param write ReservationWrite DTO containing reservation data
     * @return UUID of the newly created reservation
     */
    @PostMapping
    @Operation(summary = "Create reservation", description = "Create a new reservation and return its ID")
    public ResponseEntity<UUID> create(@Valid @RequestBody ReservationWrite write) {
        return ResponseEntity.ok( reservationService.create(write));
    }
}
