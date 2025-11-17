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

@RestController
@RequestMapping(AVAILABILITY)
@RequiredArgsConstructor
public class AvailabilityController {
    private final AvailabilityService availabilityService;

    @GetMapping
    public ResponseEntity<AvailabilityDTO> search(@ModelAttribute AvailabilityRequestDTO request) {
        return ResponseEntity.ok(availabilityService.findAvailability(request));
    }

}