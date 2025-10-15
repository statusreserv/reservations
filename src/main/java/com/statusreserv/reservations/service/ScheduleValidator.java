package com.statusreserv.reservations.service;

import com.statusreserv.reservations.model.schedule.Schedule;
import com.statusreserv.reservations.model.schedule.ScheduleTime;
import com.statusreserv.reservations.model.tenant.Tenant;
import com.statusreserv.reservations.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ScheduleValidator {

    private final ScheduleRepository repository;

    public void validateSchedule(Schedule schedule, UUID ignoreId) {
        Tenant tenant = schedule.getTenant();
        DayOfWeek day = schedule.getDayOfWeek();
        Set<ScheduleTime> newTimes = schedule.getScheduleTime();

        newTimes.forEach(this::validateTime);

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

    private boolean isOverlapping(ScheduleTime a, ScheduleTime b) {
        LocalTime startA = a.getOpenTime();
        LocalTime endA = a.getCloseTime();
        LocalTime startB = b.getOpenTime();
        LocalTime endB = b.getCloseTime();

        return startA.isBefore(endB) && startB.isBefore(endA);
    }
}
