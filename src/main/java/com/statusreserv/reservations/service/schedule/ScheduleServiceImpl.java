package com.statusreserv.reservations.service.schedule;

import com.statusreserv.reservations.dto.schedule.ScheduleDTO;
import com.statusreserv.reservations.dto.schedule.ScheduleWrite;
import com.statusreserv.reservations.mapper.ScheduleMapper;
import com.statusreserv.reservations.model.schedule.Schedule;
import com.statusreserv.reservations.repository.ScheduleRepository;
import com.statusreserv.reservations.service.auth.CurrentUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository repository;
    private final ScheduleMapper mapper;
    private final CurrentUserService currentUserService;
    private final ScheduleValidator validator;

    public List<ScheduleDTO> findAll() {
        return getAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public Set<Schedule> getAll() {
        return new HashSet<>(repository.findByTenantId(currentUserService.getCurrentTenantId()));
    }

    public ScheduleDTO findSchedule(UUID id) {
        return mapper.toDTO(getById(id));
    }

    public Schedule getById(UUID id) {
        return repository.findByIdAndTenantId(id, currentUserService.getCurrentTenantId())
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));
    }

    @Transactional
    public UUID create(ScheduleWrite write) {
        var schedule = mapper.toEntity(write, currentUserService.getCurrentTenant());
        var scheduleTimes = write.scheduleTime()
                .stream()
                .map(mapper::toEntity)
                .collect(Collectors.toSet());
        schedule.setScheduleTime(scheduleTimes);
        var entity = repository.save(schedule);
        validator.validateSchedule(entity, entity.getId());
        return entity.getId();
    }

    @Transactional
    public void update(UUID id, ScheduleWrite write) {
        var existing = getById(id);

        var scheduleTimes = write.scheduleTime()
                .stream()
                .map(mapper::toEntity)
                .collect(Collectors.toSet());

        existing.setScheduleTime(scheduleTimes);
        existing.setDayOfWeek(write.dayOfWeek());

        repository.save(existing);
    }

    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Schedule not found");
        }
        repository.deleteByIdAndTenantId(id, currentUserService.getCurrentTenantId());
    }
}
