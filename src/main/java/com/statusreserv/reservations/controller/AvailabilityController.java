package com.statusreserv.reservations.controller;

import com.statusreserv.reservations.dto.availability.AvailabilityDTO;
import com.statusreserv.reservations.dto.availability.AvailabilityRequestDTO;
import com.statusreserv.reservations.service.availability.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.statusreserv.reservations.constants.Endpoints.AVAILABILITY;

/**
 * REST controller responsible for checking available reservation slots.
 *
 * <p>Provides an endpoint to calculate availability based on selected services
 * and a date range.
 */
@RestController
@RequestMapping(AVAILABILITY)
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    /**
     * Searches for available reservation slots.
     *
     * <p>The request is sent as query parameters and mapped to {@link AvailabilityRequestDTO}.
     *
     * @param request DTO containing the date range and selected service IDs
     * @return {@link AvailabilityDTO} containing the available time slots and services
     */
    @GetMapping
    public ResponseEntity<AvailabilityDTO> search(@ModelAttribute AvailabilityRequestDTO request) {
        return ResponseEntity.ok(availabilityService.findAvailability(request));
    }
}
