package com.statusreserv.reservations.model.schedule;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalTime;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@With
public class ScheduleTime {
    @Transient
    private UUID temporaryId;
    private LocalTime openTime;
    private LocalTime closeTime;
}
