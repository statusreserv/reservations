package com.statusreserv.reservations.service.availability;

import com.statusreserv.reservations.dto.availability.AvailabilityDTO;
import com.statusreserv.reservations.dto.availability.AvailabilityRequestDTO;
import com.statusreserv.reservations.dto.availability.TimeRangeDTO;
import com.statusreserv.reservations.dto.availability.TimeSlotDTO;
import com.statusreserv.reservations.mapper.ServiceProvidedMapper;
import com.statusreserv.reservations.model.reservation.Reservation;
import com.statusreserv.reservations.model.reservation.Status;
import com.statusreserv.reservations.model.schedule.Schedule;
import com.statusreserv.reservations.model.service.ServiceProvided;
import com.statusreserv.reservations.repository.ReservationRepository;
import com.statusreserv.reservations.service.auth.CurrentUserService;
import com.statusreserv.reservations.service.schedule.ScheduleService;
import com.statusreserv.reservations.service.service.ServiceProvidedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final ServiceProvidedService serviceProvidedService;
    private final ReservationRepository reservationRepository;// TODO: Replace with service when ready
    private final CurrentUserService currentUserService;
    private final ScheduleService scheduleService;
    private final ServiceProvidedMapper serviceProvidedMapper;

    @Override
    public AvailabilityDTO findAvailability(AvailabilityRequestDTO request) {
        var services = serviceProvidedService.findByIdIn(request.services());
        var durationMinutes = getTotalDuration(services);
        var dates = getDatesBetween(request.from(), request.to());
        var periods = getPeriods(dates);
        var availableSlots = getAvailableTimeSlots(periods, durationMinutes);

        var serviceDTOList = services.stream()
                .map(serviceProvidedMapper::toDTO)
                .collect(Collectors.toSet());

        return new AvailabilityDTO(availableSlots, serviceDTOList);
    }

    /**
     * Builds a map of working time ranges for each date,
     * based on the existing schedules for the tenant.
     */
    public Map<LocalDate, List<TimeRangeDTO>> getPeriods(List<LocalDate> dates) {
        var schedules = scheduleService.getAll();
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

    /**
     * Calculates all available time slots for each date,
     * considering reservations and total service duration.
     */
    public Set<TimeSlotDTO> getAvailableTimeSlots(Map<LocalDate, List<TimeRangeDTO>> periods, int durationMinutes) {
        if (periods.isEmpty()) return Set.of();

        var startDate = periods.keySet().stream().min(LocalDate::compareTo).orElseThrow();
        var endDate = periods.keySet().stream().max(LocalDate::compareTo).orElseThrow();

        var statuses = Set.of(Status.COMPLETED, Status.PENDING, Status.CONFIRMED);
        var reservations = reservationRepository.findByDateBetweenAndStatusInAndTenantId(
                startDate,
                endDate,
                statuses,
                currentUserService.getCurrentTenantId()
        );

        var reservationsByDate = reservations.stream()
                .collect(Collectors.groupingBy(Reservation::getDate));

        var availableSlots = new LinkedHashSet<TimeSlotDTO>();

        for (var entry : periods.entrySet()) {
            var date = entry.getKey();
            var dayPeriods = entry.getValue();
            var dayReservations = reservationsByDate.getOrDefault(date, List.of());

            for (TimeRangeDTO(LocalTime start, LocalTime end) : dayPeriods) {

                while (!start.plusMinutes(durationMinutes).isAfter(end)) {
                    var slotEnd = start.plusMinutes(durationMinutes);
                    var finalCursor = start;

                    var slotTaken = dayReservations.stream().anyMatch(r ->
                            r.getStartTime().isBefore(slotEnd) &&
                                    r.getEndTime().isAfter(finalCursor)
                    );

                    if (!slotTaken) {
                        availableSlots.add(new TimeSlotDTO(date, new TimeRangeDTO(start, slotEnd)));
                    }

                    start = start.plusMinutes(durationMinutes);
                }
            }
        }

       return availableSlots.stream().sorted(
                Comparator.comparing(TimeSlotDTO::date)
                        .thenComparing(slot -> slot.timeRange().start())
        ).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Calculates the total duration of all selected services.
     */
    private int getTotalDuration(List<ServiceProvided> serviceProvidedList) {
        return serviceProvidedList.stream()
                .map(ServiceProvided::getDurationMinutes)
                .reduce(0, Integer::sum);
    }

    /**
     * Generates a list of dates between two given dates (inclusive).
     */
    private List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
        return Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startDate, endDate) + 1)
                .toList();
    }
}
