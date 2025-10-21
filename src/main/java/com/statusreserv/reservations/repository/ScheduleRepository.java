package com.statusreserv.reservations.repository;

import com.statusreserv.reservations.model.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    List<Schedule> findByTenantIdAndDayOfWeek(UUID tenantId, DayOfWeek dayOfWeek);
    List<Schedule> findByTenantId(UUID tenantId);
    Optional<Schedule> findByIdAndTenantId(UUID id, UUID tenantId);
    void deleteByIdAndTenantId(UUID id, UUID tenantId);
}
