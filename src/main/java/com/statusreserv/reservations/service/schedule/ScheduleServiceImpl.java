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

/**
 * Implementation of {@link ScheduleService} responsible for managing schedules
 * for the current tenant.
 *
 * <p>Provides operations to create, retrieve, update, and delete schedules,
 * including mapping between DTOs and entities and tenant scoping.
 */
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository repository;
    private final ScheduleMapper mapper;
    private final CurrentUserService currentUserService;
    private final ScheduleValidator validator;

    /**
     * Retrieves all schedules as DTOs for the current tenant.
     *
     * @return a list of {@link ScheduleDTO} representing all schedules
     */
    public List<ScheduleDTO> findAll() {
        return getAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all schedule entities for the current tenant.
     *
     * @return a set of {@link Schedule} entities
     */
    public Set<Schedule> getAll() {
        return new HashSet<>(repository.findByTenantId(currentUserService.getCurrentTenantId()));
    }

    /**
     * Retrieves a schedule by its UUID and maps it to DTO.
     *
     * @param id the UUID of the schedule
     * @return a {@link ScheduleDTO} representing the schedule
     * @throws EntityNotFoundException if no schedule exists with the given id
     */
    public ScheduleDTO findSchedule(UUID id) {
        return mapper.toDTO(getById(id));
    }

    /**
     * Retrieves a schedule entity by its UUID for the current tenant.
     *
     * @param id the UUID of the schedule
     * @return the {@link Schedule} entity
     * @throws EntityNotFoundException if no schedule exists with the given id
     */
    public Schedule getById(UUID id) {
        return repository.findByIdAndTenantId(id, currentUserService.getCurrentTenantId())
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));
    }

    /**
     * Creates a new schedule.
     *
     * @param write DTO containing schedule data
     * @return the UUID of the newly created schedule
     */
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

    /**
     * Updates an existing schedule with new data.
     *
     * @param id the UUID of the schedule to update
     * @param write DTO containing updated schedule data
     * @throws EntityNotFoundException if no schedule exists with the given id
     */
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

    /**
     * Deletes a schedule by its UUID for the current tenant.
     *
     * @param id the UUID of the schedule to delete
     * @throws EntityNotFoundException if no schedule exists with the given id
     */
    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Schedule not found");
        }
        repository.deleteByIdAndTenantId(id, currentUserService.getCurrentTenantId());
    }
}
