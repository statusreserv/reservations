package com.statusreserv.reservations.dto.schedule;

import java.time.DayOfWeek;
import java.util.Set;
import java.util.UUID;

public record ScheduleDTO(
        UUID id,
        DayOfWeek dayOfWeek,
        Set<ScheduleTimeDTO> scheduleTime
) {}
