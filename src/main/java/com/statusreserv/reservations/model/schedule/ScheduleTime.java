package com.statusreserv.reservations.model.schedule;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalTime;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@With
public class ScheduleTime {
    private LocalTime openTime;
    private LocalTime closeTime;
}
