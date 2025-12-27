package com.statusreserv.reservations.service.schedule;

import com.statusreserv.reservations.dto.schedule.ScheduleDTO;
import com.statusreserv.reservations.dto.schedule.ScheduleWrite;
import com.statusreserv.reservations.model.schedule.Schedule;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Service interface responsible for managing schedules.
 *
 * <p>Provides operations to create, retrieve, update, and delete schedules
 * for the current tenant.
 */
public interface ScheduleService {

    /**
     * Retrieves all schedules as DTOs.
     *
     * @return a list of {@link ScheduleDTO} representing all schedules
     */
    List<ScheduleDTO> findAll();

    /**
     * Retrieves all schedules as entities.
     *
     * @return a set of {@link Schedule} entities
     */
    Set<Schedule> getAll();

    /**
     * Retrieves a schedule by its UUID and maps it to DTO.
     *
     * @param id the UUID of the schedule
     * @return a {@link ScheduleDTO} representing the schedule
     *
     */
    ScheduleDTO findSchedule(UUID id);

    /**
     * Retrieves a schedule entity by its UUID.
     *
     * @param id the UUID of the schedule
     * @return the {@link Schedule} entity
     *
     */
    Schedule getById(UUID id);

    /**
     * Creates a new schedule.
     *
     * @param write DTO containing schedule data
     * @return the UUID of the newly created schedule
     */
    UUID create(ScheduleWrite write);

    /**
     * Updates an existing schedule with new data.
     *
     * @param id the UUID of the schedule to update
     * @param write DTO containing updated schedule data
     *
     */
    void update(UUID id, ScheduleWrite write);

    /**
     * Deletes a schedule by its UUID.
     *
     * @param id the UUID of the schedule to delete
     *
     */
    void delete(UUID id);
}
