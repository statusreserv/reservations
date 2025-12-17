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

        var totalDuration = reservation.getReservationServices()
                .stream()
                .mapToInt(ReservationServiceProvided::getDurationMinutes)
                .sum();

        var schedules = scheduleRepository.findByTenantIdAndDayOfWeek(reservation.getTenant().getId(), reservation.getDate().getDayOfWeek());

        checkWithinSchedule(schedules, reservation);

        var existing = reservationRepository.findByTenantIdAndDate(reservation.getTenant().getId(), reservation.getDate())
                .stream()
                .filter(reservation1 -> !reservation1.getId().equals(ignoreId))
                .toList();

        checkOverlap(existing, reservation);

        var busyPeriods = existing
                .stream()
                .map(reservation1 -> new TimeRangeDTO(reservation1.getStartTime(), reservation1.getEndTime()))
                .toList();

        var availableTimeSlots = availabilityService.getAvailableTimeSlots(
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
        var withinSchedule = schedules.stream().anyMatch(
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
        var overlaps = existing.stream().anyMatch(
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
        var isAvailable = availableTimeSlots.stream().anyMatch(
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