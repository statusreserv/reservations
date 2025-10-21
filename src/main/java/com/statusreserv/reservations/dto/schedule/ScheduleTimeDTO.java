package com.statusreserv.reservations.dto.schedule;

import java.time.LocalTime;

public record ScheduleTimeDTO(
        LocalTime openTime,
        LocalTime closeTime
) {}
