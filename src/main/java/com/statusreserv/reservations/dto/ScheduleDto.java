package com.statusreserv.reservations.dto;

import java.time.DayOfWeek;
import java.util.Set;
import java.util.UUID;

public record ScheduleDto(
        UUID id,
        DayOfWeek dayOfWeek,
        Set<ScheduleTimeDto> scheduleTime
) {}
