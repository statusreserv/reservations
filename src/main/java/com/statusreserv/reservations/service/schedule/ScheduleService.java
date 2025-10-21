package com.statusreserv.reservations.service.schedule;

import com.statusreserv.reservations.dto.ScheduleDTO;
import com.statusreserv.reservations.dto.ScheduleWrite;
import com.statusreserv.reservations.model.schedule.Schedule;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ScheduleService {

    List<ScheduleDTO> findAll();

    Set<Schedule> getAll();

    ScheduleDTO findSchedule(UUID id);

    Schedule getById(UUID id);

    UUID create(ScheduleWrite write);

    void update(UUID id, ScheduleWrite write);

    void delete(UUID id);
}
