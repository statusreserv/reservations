package com.statusreserv.reservations.service.schedule;

import com.statusreserv.reservations.dto.schedule.ScheduleDTO;
import com.statusreserv.reservations.dto.schedule.ScheduleWrite;
import com.statusreserv.reservations.model.schedule.Schedule;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**

 * Service interface responsible for managing schedules for the current tenant.
 *
 * <p>Provides operations to retrieve, update (upsert), and delete schedules,
 * including mapping between DTOs and entities.</p>
 */
public interface ScheduleService {

    /**

     * Retrieves all schedules for the current tenant and maps them to DTOs.
     *
     * @return a list of {@link ScheduleDTO} representing all schedules
     */
    List<ScheduleDTO> findAll();

    /**

     * Retrieves all schedule entities scoped to the current tenant.
     *
     * @return a set of {@link Schedule} entities
     */
    Set<Schedule> getAll();

    /**

     * Retrieves all schedule entities scoped to the current tenant.
     *
     * @return a set of {@link Schedule} entities
     */
    Set<Schedule> getAllByTenantId(UUID tenantId);

    /**

     * Retrieves a schedule for a given day of week and maps it to a DTO.
     *
     * @param dayOfWeek the day of week of the schedule
     * @return a {@link ScheduleDTO} representing the schedule
     */
    ScheduleDTO findSchedule(DayOfWeek dayOfWeek);

    /**

     * Retrieves a schedule entity for a given day of week.
     *
     * @param dayOfWeek the day of week of the schedule
     * @return the {@link Schedule} entity
     */
    Schedule getByDayOfWeek(DayOfWeek dayOfWeek);

    /**

     * Updates existing schedules or creates new ones if they do not exist,
     * based on the provided day of week.
     *
     * <p>This method performs an upsert operation for each entry.</p>
     *
     * @param write set of DTOs containing schedule data
     */
    void update(Set<ScheduleWrite> write);

    /**

     * Deletes a schedule by its UUID.
     *
     * @param id the UUID of the schedule to delete
     */
    void delete(UUID id);
}
