package com.statusreserv.reservations.service;

import com.statusreserv.reservations.dto.ScheduleDto;
import com.statusreserv.reservations.dto.ScheduleWrite;
import com.statusreserv.reservations.model.schedule.Schedule;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public interface ScheduleService {

    List<ScheduleDto> findAll();

    Set<Schedule> getAll();

    ScheduleDto findSchedule(UUID id);

    Schedule getById(UUID id);

    UUID create(ScheduleWrite write);

    ResponseEntity<Schedule> update(UUID id, ScheduleWrite write);

    void delete(UUID id);
}
