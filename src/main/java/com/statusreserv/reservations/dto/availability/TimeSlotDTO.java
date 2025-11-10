package com.statusreserv.reservations.dto.availability;

import java.time.LocalDate;

public record TimeSlotDTO(LocalDate date, TimeRangeDTO timeRange) {}
