package com.statusreserv.reservations.dto;

import java.time.DayOfWeek;
import java.util.Set;
import java.util.UUID;

public record ScheduleDTO(
        UUID id,
        DayOfWeek dayOfWeek,
        Set<ScheduleTimeDTO> scheduleTime
) {}
