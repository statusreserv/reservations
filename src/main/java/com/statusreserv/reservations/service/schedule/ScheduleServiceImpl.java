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

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

/**

 * Implementation of {@link ScheduleService} responsible for managing schedules
 * for the current tenant.
 *
 * <p>Provides operations to retrieve, update (upsert), and delete schedules,
 * including mapping between DTOs and entities and tenant scoping.</p>
 */
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository repository;
    private final ScheduleMapper mapper;
    private final CurrentUserService currentUserService;
    private final ScheduleValidator validator;

    /**

     * Retrieves all schedules for the current tenant and maps them to DTOs.
     *
     * @return a list of {@link ScheduleDTO} representing all schedules
     */
    @Override
    public List<ScheduleDTO> findAll() {
        return getAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**

     * Retrieves all schedule entities scoped to the current tenant.
     *
     * @return a set of {@link Schedule} entities
     */
    @Override
    public Set<Schedule> getAll() {
        return new HashSet<>(getAllByTenantId(currentUserService.getCurrentTenantId()));
    }

    @Override
    public Set<Schedule> getAllByTenantId(UUID tenantId) {
        return new HashSet<>(repository.findByTenantId(tenantId));
    }

    /**

     * Retrieves a schedule for a given day of week for the current tenant
     * and maps it to a DTO.
     *
     * @param dayOfWeek the day of week of the schedule
     * @return a {@link ScheduleDTO} representing the schedule
     * @throws EntityNotFoundException if no schedule exists for the given day
     */
    @Override
    public ScheduleDTO findSchedule(DayOfWeek dayOfWeek) {
        return mapper.toDTO(getByDayOfWeek(dayOfWeek));
    }

    /**

     * Retrieves a schedule entity for a given day of week
     * scoped to the current tenant.
     *
     * @param dayOfWeek the day of week of the schedule
     * @return the {@link Schedule} entity
     * @throws EntityNotFoundException if no schedule exists for the given day
     */
    @Override
    public Schedule getByDayOfWeek(DayOfWeek dayOfWeek) {
        return getOptionalByDayOfWeek(dayOfWeek)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));
    }

    /**

     * Creates a new schedule for the current tenant.
     *
     * <p>This method is intentionally private and is used internally
     * to support an upsert-like behavior in {@link #update(Set)}.</p>
     *
     * @param write DTO containing schedule data
     * @return the persisted {@link Schedule} entity
     */
    private Schedule create(ScheduleWrite write) {
        var schedule = mapper.toEntity(write, currentUserService.getCurrentTenant());
        var scheduleTimes = write.scheduleTime()
                .stream()
                .map(mapper::toEntity)
                .collect(Collectors.toSet());
        schedule.setScheduleTime(scheduleTimes);
        return repository.save(schedule);
    }

    /**

     * Updates existing schedules or creates new ones if they do not exist,
     * based on the provided day of week, for the current tenant.
     *
     * <p>This method performs an upsert operation for each entry.</p>
     *
     * @param writes set of DTOs containing schedule data
     */
    @Transactional
    public void update(Set<ScheduleWrite> writes) {
        writes.forEach(write -> {
            var optionalSchedule = getOptionalByDayOfWeek(write.dayOfWeek());
            var updatedSchedule = optionalSchedule
                    .map((schedule) -> update(schedule, write))
                    .orElseGet(() -> create(write));
            validator.validateSchedule(updatedSchedule);
        });
    }

    /**

     * Updates an existing schedule entity with new data.
     *
     * @param schedule the existing schedule entity
     * @param write DTO containing updated schedule data
     * @return the updated {@link Schedule} entity
     */
    private Schedule update(Schedule schedule, ScheduleWrite write) {
        var scheduleTimes = write.scheduleTime()
                .stream()
                .map(mapper::toEntity)
                .collect(Collectors.toSet());

        schedule.setScheduleTime(scheduleTimes);
        schedule.setDayOfWeek(write.dayOfWeek());
        repository.save(schedule);
        return schedule;
    }

    /**

     * Deletes a schedule by its UUID for the current tenant.
     *
     * @param id the UUID of the schedule to delete
     * @throws EntityNotFoundException if no schedule exists with the given id
     */
    @Transactional
    @Override
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Schedule not found");
        }
        repository.deleteByIdAndTenantId(id, currentUserService.getCurrentTenantId());
    }

    /**

     * Retrieves a schedule for a given day of week for the current tenant,
     * wrapped in an {@link Optional}.
     *
     * @param dayOfWeek the day of week of the schedule
     * @return an {@link Optional} containing the schedule if found
     */
    private Optional<Schedule> getOptionalByDayOfWeek(DayOfWeek dayOfWeek) {
        return repository.findByTenantIdAndDayOfWeek(currentUserService.getCurrentTenantId(), dayOfWeek);
    }
}
