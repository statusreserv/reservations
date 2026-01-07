package com.statusreserv.reservations.service.schedule;

import com.statusreserv.reservations.model.schedule.Schedule;
import com.statusreserv.reservations.model.schedule.ScheduleTime;
import com.statusreserv.reservations.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Validator responsible for ensuring schedule integrity for a tenant.
 *
 * <p>Checks that:
 * <ul>
 *     <li>All ScheduleTime entries have valid open and close times</li>
 *     <li>Open times are before close times</li>
 *     <li>No overlapping schedules exist for the same tenant on the same day</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class ScheduleValidator {

    private final ScheduleRepository repository;

    /**
     * Validates a schedule before creation or update.
     *
     * @param schedule the {@link Schedule} to validate
     * @param ignoreId the ID of a schedule to ignore (useful when updating an existing schedule)
     * @throws IllegalArgumentException if any validation fails
     */
    public void validateSchedule(Schedule schedule, UUID ignoreId) {
        var tenant = schedule.getTenant();
        var day = schedule.getDayOfWeek();
        var newTimes = schedule.getScheduleTime();

        // Validate individual schedule times
        newTimes.forEach(this::validateTime);

        // Check for overlapping schedules
        repository.findByTenantIdAndDayOfWeek(tenant.getId(), day)
                .stream()
                .filter(existing -> !existing.getId().equals(ignoreId))
                .forEach(existing -> {
                    for (ScheduleTime existingTime : existing.getScheduleTime()) {
                        for (ScheduleTime newTime : newTimes) {
                            if (isOverlapping(existingTime, newTime)) {
                                throw new IllegalArgumentException(
                                        "Schedule conflict: overlapping time for tenant "
                                                + tenant.getId() + " on " + day
                                );
                            }
                        }
                    }
                });
    }

    /**
     * Validates a single {@link ScheduleTime}.
     *
     * @param time the {@link ScheduleTime} to validate
     * @throws IllegalArgumentException if openTime or closeTime is null, or openTime is not before closeTime
     */
    private void validateTime(ScheduleTime time) {
        if (time.getOpenTime() == null || time.getCloseTime() == null) {
            throw new IllegalArgumentException("ScheduleTime must have both openTime and closeTime");
        }
        if (!time.getOpenTime().isBefore(time.getCloseTime())) {
            throw new IllegalArgumentException(
                    "Invalid ScheduleTime: openTime (" + time.getOpenTime() +
                            ") must be before closeTime (" + time.getCloseTime() + ")"
            );
        }
    }

    /**
     * Checks if two {@link ScheduleTime} entries overlap.
     *
     * @param a first schedule time
     * @param b second schedule time
     * @return true if the times overlap, false otherwise
     */
    private boolean isOverlapping(ScheduleTime a, ScheduleTime b) {
        var startA = a.getOpenTime();
        var endA = a.getCloseTime();
        var startB = b.getOpenTime();
        var endB = b.getCloseTime();

        return startA.isBefore(endB) && startB.isBefore(endA);
    }
}
