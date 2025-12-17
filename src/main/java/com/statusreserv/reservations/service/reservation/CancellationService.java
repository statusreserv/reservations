package com.statusreserv.reservations.service.reservation;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface CancellationService {
    void cancelReservation(UUID id);
}