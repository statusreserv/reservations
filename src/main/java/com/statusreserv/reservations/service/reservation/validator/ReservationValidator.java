package com.statusreserv.reservations.service.reservation.validator;

import com.statusreserv.reservations.dto.availability.TimeRangeDTO;
import com.statusreserv.reservations.dto.availability.TimeSlotDTO;
import com.statusreserv.reservations.model.reservation.Reservation;
import com.statusreserv.reservations.model.reservation.ReservationServiceProvided;
import com.statusreserv.reservations.model.reservation.ReservationStatus;
import com.statusreserv.reservations.model.schedule.Schedule;
import com.statusreserv.reservations.repository.ReservationRepository;
import com.statusreserv.reservations.service.availability.AvailabilityService;
import com.statusreserv.reservations.service.schedule.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Validates reservations against schedule, availability, and overlapping constraints.
 */
@Component
@RequiredArgsConstructor
public class ReservationValidator {

    private final ReservationRepository reservationRepository;
    private final AvailabilityService availabilityService;
    private final ScheduleService scheduleService;

    /**
     * Validates a reservation, checking services, schedule, overlaps, and available time slots.
     *
     * @param reservation Reservation to validate
     * @param ignoreId ID to ignore during overlap checks (for updates)
     */
    public void validateReservation(Reservation reservation, UUID ignoreId) {
        reservation.getReservationServices().forEach(this::validateServiceTime);

        var totalDuration = reservation.getReservationServices()
                .stream()
                .mapToInt(ReservationServiceProvided::getDurationMinutes)
                .sum();

        var schedule = scheduleService.getByDayOfWeek(reservation.getDate().getDayOfWeek());

        checkWithinSchedule(schedule, reservation);

        var existingReservations = reservationRepository.findByTenantIdAndDateAndStatusNotIn(
                reservation.getTenant().getId(),
                        reservation.getDate(),
                Set.of(ReservationStatus.CANCELLED, ReservationStatus.EXPIRED))
                .stream()
                .filter(r -> !r.getId().equals(ignoreId))
                .toList();

        var timeRange = new TimeRangeDTO(reservation.getStartTime(), reservation.getEndTime());

        var availableTimeSlots = availabilityService.getAvailableTimeSlots(
                Map.of(reservation.getDate(), List.of(timeRange)),
                totalDuration,
                reservation.getTenant().getId(),
                reservation.getId()
        );

        checkOverlap(existingReservations, reservation);
        checkAvailableTimeSlots(availableTimeSlots, reservation);
    }

    private void validateServiceTime(ReservationServiceProvided service) {
        if (!service.getReservation().getStartTime().isBefore(service.getReservation().getEndTime())) {
            throw new IllegalArgumentException(String.format(
                    "Service has invalid time range: %s to %s.",
                    service.getReservation().getStartTime(),
                    service.getReservation().getEndTime()
            ));
        }
    }

    private void checkWithinSchedule(Schedule schedule, Reservation reservation) {
        var withinSchedule = schedule.getScheduleTime().stream().anyMatch(
                        time -> !reservation.getStartTime().isBefore(time.getOpenTime()) &&
                                !reservation.getEndTime().isAfter(time.getCloseTime())
        );

        if (!withinSchedule) {
            throw new IllegalArgumentException(String.format(
                    "Reservation from %s to %s on %s is outside working hours.",
                    reservation.getStartTime(),
                    reservation.getEndTime(),
                    reservation.getDate()
            ));
        }
    }

    public void checkOverlap(List<Reservation> existing, Reservation reservation) {
        var overlaps = existing.stream().anyMatch(
                r -> reservation.getStartTime().isBefore(r.getEndTime()) &&
                        reservation.getEndTime().isAfter(r.getStartTime())
        );

        if (overlaps) {
            throw new IllegalArgumentException(String.format(
                    "Reservation from %s to %s on %s overlaps with existing reservation.",
                    reservation.getStartTime(),
                    reservation.getEndTime(),
                    reservation.getDate()
            ));
        }
    }

    private void checkAvailableTimeSlots(Set<TimeSlotDTO> availableTimeSlots, Reservation reservation) {
        var isUnavailable = availableTimeSlots.stream().noneMatch(
                slot -> !reservation.getStartTime().isBefore(slot.timeRange().start()) &&
                        !reservation.getEndTime().isAfter(slot.timeRange().end())
        );

        if (isUnavailable) {
            throw new IllegalArgumentException(String.format(
                    "Chosen time %s to %s for reservation on %s is not available.",
                    reservation.getStartTime(),
                    reservation.getEndTime(),
                    reservation.getDate()
            ));
        }
    }
}
