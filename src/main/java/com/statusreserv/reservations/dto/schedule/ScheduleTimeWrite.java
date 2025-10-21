package com.statusreserv.reservations.dto.schedule;

import java.time.LocalTime;

public record ScheduleTimeWrite(
        LocalTime openTime,
        LocalTime closeTime
) {}
