package com.statusreserv.reservations.controller;

import com.statusreserv.reservations.service.reservation.CancellationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class CancellationController {
    private final CancellationService cancellationService;


    @PatchMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable UUID id) {
        cancellationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }


}
