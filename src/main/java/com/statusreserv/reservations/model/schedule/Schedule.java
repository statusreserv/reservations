package com.statusreserv.reservations.model.schedule;

import com.statusreserv.reservations.model.tenant.Tenant;
import jakarta.persistence.*;
import lombok.*;
import java.time.DayOfWeek;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@With
@Table(name = "tbl_schedules")
public class Schedule {
    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @ElementCollection
    @CollectionTable(
            name = "tbl_schedule_times",
            joinColumns = @JoinColumn(name = "schedule_id")
    )
    private Set<ScheduleTime> scheduleTime;

    private Instant createdAt = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY)
    private Tenant tenant;
}
