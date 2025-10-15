package com.statusreserv.reservations.service;

import com.statusreserv.reservations.dto.ScheduleDto;
import com.statusreserv.reservations.dto.ScheduleWrite;
import com.statusreserv.reservations.mapper.ScheduleMapper;
import com.statusreserv.reservations.model.schedule.Schedule;
import com.statusreserv.reservations.model.tenant.Tenant;
import com.statusreserv.reservations.repository.ScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImp implements ScheduleService {

    private final ScheduleRepository repository;
    private final ScheduleMapper mapper;

    public List<ScheduleDto> findAll() {
        return getAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public Set<Schedule> getAll() {
        return new HashSet<>(repository.findAll());
    }

    public ScheduleDto findSchedule(UUID id) {
        return mapper.toDTO(getById(id));
    }

    public Schedule getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));
    }

    public UUID create(ScheduleWrite write) {
        var schedule = mapper.toEntity(write, new Tenant());// TODO: Temporary placeholder. Replace with the tenant from context to use the actual tenant.
        var scheduleTimes = write.scheduleTime()
                .stream()
                .map(mapper::toEntity)
                .collect(Collectors.toSet());
        schedule.setScheduleTime(scheduleTimes);
        return repository.save(schedule).getId();
    }


    public ResponseEntity<Schedule> update(UUID id, ScheduleWrite write) {
        Schedule existing = getById(id);

        var scheduleTimes = write.scheduleTime()
                .stream()
                .map(mapper::toEntity)
                .collect(Collectors.toSet());

        existing.setScheduleTime(scheduleTimes);
        existing.setDayOfWeek(write.dayOfWeek());

        return ResponseEntity.ok(repository.save(existing));
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Schedule not found");
        }
        repository.deleteById(id);
    }
}
