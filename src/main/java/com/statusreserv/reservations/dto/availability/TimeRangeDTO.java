package com.statusreserv.reservations.dto.availability;

import java.time.LocalTime;

public record TimeRangeDTO(LocalTime start, LocalTime end) {}
