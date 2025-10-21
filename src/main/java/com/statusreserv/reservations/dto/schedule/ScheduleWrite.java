package com.statusreserv.reservations.dto.schedule;

import java.time.DayOfWeek;
import java.util.Set;

public record ScheduleWrite(
        DayOfWeek dayOfWeek,
        Set<ScheduleTimeWrite> scheduleTime
) {}
