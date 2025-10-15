package com.statusreserv.reservations.dto;

import java.time.LocalTime;

public record ScheduleTimeWrite(
        LocalTime openTime,
        LocalTime closeTime
) {}
