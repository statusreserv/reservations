package com.statusreserv.reservations.dto.availability;

import com.statusreserv.reservations.dto.service.ServiceProvidedDTO;

import java.util.Set;

public record AvailabilityDTO(
        Set<TimeSlotDTO> timeSlots,
        Set<ServiceProvidedDTO> services) {
}
