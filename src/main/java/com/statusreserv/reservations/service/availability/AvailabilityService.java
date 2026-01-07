package com.statusreserv.reservations.service.availability;

import com.statusreserv.reservations.dto.availability.AvailabilityDTO;
import com.statusreserv.reservations.dto.availability.AvailabilityRequestDTO;
import com.statusreserv.reservations.dto.availability.TimeRangeDTO;
import com.statusreserv.reservations.dto.availability.TimeSlotDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service responsible for handling availability and time slot calculations.
 */
public interface AvailabilityService {

    /**
     * Finds availability based on the provided search criteria.
     *
     * @param request AvailabilityRequestDTO containing search parameters
     * @return AvailabilityDTO with available time slots and resources
     */
    AvailabilityDTO findAvailability(AvailabilityRequestDTO request);

    /**
     * Calculates available time slots given existing busy periods and desired duration.
     *
     * @param periods Map of dates to their corresponding busy time ranges
     * @param durationMinutes Duration in minutes for each desired time slot
     * @return Set of TimeSlotDTO representing all available time slots
     */
    Set<TimeSlotDTO> getAvailableTimeSlots(Map<LocalDate, List<TimeRangeDTO>> periods, int durationMinutes);

}
