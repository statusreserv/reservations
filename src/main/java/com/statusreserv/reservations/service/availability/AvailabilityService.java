package com.statusreserv.reservations.service.availability;

import com.statusreserv.reservations.dto.availability.AvailabilityDTO;
import com.statusreserv.reservations.dto.availability.AvailabilityRequestDTO;

public interface AvailabilityService {

    AvailabilityDTO findAvailability(AvailabilityRequestDTO request);

}
