package com.statusreserv.reservations.service.availability;

import com.statusreserv.reservations.dto.availability.AvailabilityDTO;
import com.statusreserv.reservations.dto.availability.AvailabilityRequestDTO;
import com.statusreserv.reservations.dto.availability.TimeRangeDTO;
import com.statusreserv.reservations.dto.availability.TimeSlotDTO;
import com.statusreserv.reservations.mapper.ServiceProvidedMapper;
import com.statusreserv.reservations.model.reservation.Reservation;
import com.statusreserv.reservations.model.reservation.ReservationStatus;
import com.statusreserv.reservations.model.schedule.Schedule;
import com.statusreserv.reservations.repository.ReservationRepository;
import com.statusreserv.reservations.repository.service.ServiceProvided;
import com.statusreserv.reservations.service.auth.CurrentUserService;
import com.statusreserv.reservations.service.schedule.ScheduleService;
import com.statusreserv.reservations.service.service.ServiceProvidedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of {@link AvailabilityService} responsible for calculating
 * available reservation slots.
 *
 * <p>Availability is computed considering:
 * <ul>
 *     <li>Operating schedules (via {@link ScheduleService})</li>
 *     <li>Existing reservations (via {@link ReservationRepository})</li>
 *     <li>Total duration of selected services (via {@link ServiceProvidedService})</li>
 * </ul>
 *
 * <p>The current tenant is resolved using {@link CurrentUserService}, and service
 * details are mapped with {@link ServiceProvidedMapper}.
 */
@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final ServiceProvidedService serviceProvidedService;
    private final ReservationRepository reservationRepository;
    private final ScheduleService scheduleService;
    private final ServiceProvidedMapper serviceProvidedMapper;

    /**
     * Finds available reservation slots for the given request.
     *
     * @param request DTO containing the date range and selected service IDs
     * @return {@link AvailabilityDTO} with available time slots and corresponding services
     */
    @Override
    public AvailabilityDTO findAvailability(AvailabilityRequestDTO request, UUID tenantId) {
        var services = serviceProvidedService.findByIdInAndTenantId(request.services(), tenantId);
        var durationMinutes = getTotalDuration(services);
        var dates = getDatesBetween(request.from(), request.to());
        var periods = getPeriods(dates, tenantId);
        var availableSlots = getAvailableTimeSlots(periods, durationMinutes, tenantId);

        var serviceDTOList = services.stream()
                .map(serviceProvidedMapper::toDTO)
                .collect(Collectors.toSet());

        return new AvailabilityDTO(availableSlots, serviceDTOList);
    }

    /**
     * Constructs a map of operating periods by date based on existing schedules.
     *
     * @param dates list of dates to compute periods for
     * @return map with {@link LocalDate} as key and {@link List} of {@link TimeRangeDTO} as value
     */
    public Map<LocalDate, List<TimeRangeDTO>> getPeriods(List<LocalDate> dates, UUID tenantId) {
        var schedules = scheduleService.getAllByTenantId(tenantId);
        var timeSlotsPerDate = new HashMap<LocalDate, List<TimeRangeDTO>>();

        var schedulesDays = schedules.stream()
                .map(Schedule::getDayOfWeek)
                .toList();

        dates.stream()
                .filter(date -> schedulesDays.contains(date.getDayOfWeek()))
                .forEach(date -> {
                    var scheduleTimeRanges = schedules.stream()
                            .filter(schedule -> schedule.getDayOfWeek().equals(date.getDayOfWeek()))
                            .flatMap(schedule -> schedule.getScheduleTime().stream())
                            .map(scheduleTime ->
                                    new TimeRangeDTO(scheduleTime.getOpenTime(), scheduleTime.getCloseTime())
                            )
                            .toList();

                    timeSlotsPerDate.put(date, scheduleTimeRanges);
                });

        return timeSlotsPerDate;
    }

    public Set<TimeSlotDTO> getAvailableTimeSlots(
            Map<LocalDate, List<TimeRangeDTO>> periods,
            int durationMinutes,
            UUID tenantId
    )  {
        return getAvailableTimeSlots(periods, durationMinutes, tenantId, null);
    }

    /**
     * Computes available time slots based on operating periods, existing reservations,
     * and desired duration.
     *
     * @param periods map of dates to operating periods
     * @param durationMinutes total duration of selected services in minutes
     * @return set of {@link TimeSlotDTO} representing available slots
     */
    public Set<TimeSlotDTO> getAvailableTimeSlots(
            Map<LocalDate, List<TimeRangeDTO>> periods,
            int durationMinutes,
            UUID tenantId,
            UUID ignoreReservationId
    ) {
        if (periods.isEmpty() || durationMinutes <= 0) return Set.of();

        var startDate = periods.keySet().stream().min(LocalDate::compareTo).orElseThrow();
        var endDate = periods.keySet().stream().max(LocalDate::compareTo).orElseThrow();

        var statuses = Set.of(
                ReservationStatus.COMPLETED,
                ReservationStatus.PENDING,
                ReservationStatus.CONFIRMED
        );

        var reservationsByDate = reservationRepository
                .findByDateBetweenAndStatusInAndTenantId(startDate, endDate, statuses, tenantId)
                .stream()
                .filter(reservation -> !reservation.getId().equals(ignoreReservationId))
                .collect(Collectors.groupingBy(Reservation::getDate));

        var availableSlots = new LinkedHashSet<TimeSlotDTO>();

        for (var entry : periods.entrySet()) {
            var date = entry.getKey();
            var dayPeriods = entry.getValue();
            var dayReservations = reservationsByDate.getOrDefault(date, List.of());

            for (var period : dayPeriods) {
                var cursor = period.start();
                var periodEnd = period.end();

                if (!cursor.isBefore(periodEnd)) continue;

                while (true) {
                    var slotEnd = cursor.plusMinutes(durationMinutes);
                    if (slotEnd.isAfter(periodEnd)) break;

                    var taken = false;
                    for (var r : dayReservations) {
                        if (r.getStartTime().isBefore(slotEnd)
                                && r.getEndTime().isAfter(cursor)) {
                            taken = true;
                            break;
                        }
                    }

                    if (!taken) {
                        availableSlots.add(
                                new TimeSlotDTO(date, new TimeRangeDTO(cursor, slotEnd))
                        );
                    }

                    cursor = slotEnd;
                }
            }
        }

        return availableSlots;
    }

    /**
     * Calculates total duration of all selected services.
     *
     * @param serviceProvidedList list of selected services
     * @return total duration in minutes
     */
    private int getTotalDuration(List<ServiceProvided> serviceProvidedList) {
        return serviceProvidedList.stream()
                .map(ServiceProvided::getDurationMinutes)
                .reduce(0, Integer::sum);
    }

    /**
     * Generates a list of dates between two dates (inclusive).
     *
     * @param startDate start date
     * @param endDate end date
     * @return list of {@link LocalDate} between startDate and endDate
     */
    private List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
        return Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startDate, endDate) + 1)
                .toList();
    }
}
