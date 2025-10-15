package com.statusreserv.reservations.dto;

import java.time.LocalTime;
import java.util.UUID;

public record ScheduleTimeDto(
        LocalTime openTime,
        LocalTime closeTime
) {}
