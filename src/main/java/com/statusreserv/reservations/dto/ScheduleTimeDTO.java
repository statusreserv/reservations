package com.statusreserv.reservations.dto;

import java.time.LocalTime;

public record ScheduleTimeDTO(
        LocalTime openTime,
        LocalTime closeTime
) {}
