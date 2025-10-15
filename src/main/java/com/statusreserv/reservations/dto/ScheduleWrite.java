package com.statusreserv.reservations.dto;

import java.time.DayOfWeek;
import java.util.Set;
import java.util.UUID;

public record ScheduleWrite(
        DayOfWeek dayOfWeek,
        Set<ScheduleTimeWrite> scheduleTime
) {}
