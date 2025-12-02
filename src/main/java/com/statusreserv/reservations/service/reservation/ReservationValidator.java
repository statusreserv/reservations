package com.statusreserv.reservations.service.reservation;

import com.statusreserv.reservations.dto.availability.TimeRangeDTO;
import com.statusreserv.reservations.dto.availability.TimeSlotDTO;
import com.statusreserv.reservations.model.reservation.Reservation;
import com.statusreserv.reservations.model.reservation.ReservationServiceProvided;
import com.statusreserv.reservations.model.schedule.Schedule;
import com.statusreserv.reservations.repository.ReservationRepository;
import com.statusreserv.reservations.repository.ScheduleRepository;
import com.statusreserv.reservations.service.availability.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReservationValidator {

    private final ReservationRepository reservationRepository;
    private final AvailabilityService availabilityService;
    private final ScheduleRepository scheduleRepository;

    public void validateReservation(Reservation reservation, UUID ignoreId) {

        reservation.getReservationServices().forEach(this::validateReservationServiceProvided);

        int totalDuration = reservation.getReservationServices()
                .stream()
                .mapToInt(ReservationServiceProvided::getDurationMinutes)
                .sum();

        List<Schedule> schedules = scheduleRepository.findByTenantIdAndDayOfWeek(reservation.getTenant().getId(), reservation.getDate().getDayOfWeek());

        checkWithinSchedule(schedules, reservation);

        //Searches BD for all the reservations on that day that are not the one we're trying to create(ignoredID)
        List<Reservation> existing = reservationRepository.findByTenantIdAndDate(reservation.getTenant().getId(), reservation.getDate())
                .stream()
                .filter(reservation1 -> !reservation1.getId().equals(ignoreId))
                .toList();

        checkOverlap(existing, reservation);

        //Creates a List of all the busyPeriods on that day
        List<TimeRangeDTO> busyPeriods = existing
                .stream()
                .map(reservation1 -> new TimeRangeDTO(reservation1.getStartTime(), reservation1.getEndTime()))
                .toList();

        //Creates a List of availableTimeSlots that are ok to create the reservation
        Set<TimeSlotDTO> availableTimeSlots = availabilityService.getAvailableTimeSlots(
                Map.of(reservation.getDate(), busyPeriods),
                totalDuration
        );

        checkAvailableTimeSlots(availableTimeSlots, reservation);
    }

    private void validateReservationServiceProvided(ReservationServiceProvided rsp) {
        if (!rsp.getReservation().getStartTime().isBefore(rsp.getReservation().getEndTime())) {
            throw new IllegalArgumentException(
                    String.format(
                            "Service has invalid time range: %s to %s.",
                            rsp.getReservation().getStartTime(),
                            rsp.getReservation().getEndTime()
                    )
            );

        }
    }

    private void checkWithinSchedule(List<Schedule> schedules, Reservation reservation) {
        boolean withinSchedule = schedules.stream().anyMatch(
                schedule -> schedule.getScheduleTime().stream().anyMatch(
                        scheduleTime -> !reservation.getStartTime().isBefore(scheduleTime.getOpenTime()) &&
                                !reservation.getEndTime().isAfter(scheduleTime.getCloseTime())));


        if (!withinSchedule) {
            throw new IllegalArgumentException(
                    String.format(
                            "Reservation from %s to %s on %s is outside working hours.",
                            reservation.getStartTime(),
                            reservation.getEndTime(),
                            reservation.getDate()
                    )
            );
        }
    }

    private void checkOverlap(List<Reservation> existing, Reservation reservation) {
        boolean overlaps = existing.stream().anyMatch(
                r -> reservation.getStartTime().isBefore(r.getEndTime()) &&
                        reservation.getEndTime().isAfter(r.getStartTime())
        );

        if (overlaps) {
            throw new IllegalArgumentException(
                    String.format(
                            "Reservation from %s to %s on %s overlaps with existing reservation.",
                            reservation.getStartTime(),
                            reservation.getEndTime(),
                            reservation.getDate().toString()));
        }
    }

    private void checkAvailableTimeSlots(Set<TimeSlotDTO> availableTimeSlots, Reservation reservation) {
        boolean isAvailable = availableTimeSlots.stream().anyMatch(
                slot -> !reservation.getStartTime().isBefore(slot.timeRange().start()) &&
                        !reservation.getEndTime().isAfter(slot.timeRange().end()));

        if (isAvailable) {
            throw new IllegalArgumentException(
                    String.format("Chosen time %s to %s for reservation on %s is not available.",
                            reservation.getStartTime(),
                            reservation.getEndTime(),
                            reservation.getDate()
                    ));
        }
    }

}