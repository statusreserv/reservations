package com.statusreserv.reservations.repository;

import com.statusreserv.reservations.model.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    List<Schedule> findByTenantAndDayOfWeek(UUID tenantId, DayOfWeek dayOfWeek);
}
