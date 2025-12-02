package com.statusreserv.reservations.service.availability;

import com.statusreserv.reservations.dto.availability.AvailabilityDTO;
import com.statusreserv.reservations.dto.availability.AvailabilityRequestDTO;
import com.statusreserv.reservations.dto.availability.TimeRangeDTO;
import com.statusreserv.reservations.dto.availability.TimeSlotDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AvailabilityService {

    AvailabilityDTO findAvailability(AvailabilityRequestDTO request);

    Set<TimeSlotDTO> getAvailableTimeSlots(Map<LocalDate, List<TimeRangeDTO>> periods, int durationMinutes);

}
