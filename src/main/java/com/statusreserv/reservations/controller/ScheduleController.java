package com.statusreserv.reservations.controller;

import com.statusreserv.reservations.dto.schedule.ScheduleDTO;
import com.statusreserv.reservations.dto.schedule.ScheduleWrite;
import com.statusreserv.reservations.service.schedule.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.statusreserv.reservations.constants.Endpoints.ID;
import static com.statusreserv.reservations.constants.Endpoints.SCHEDULE;

/**
 * REST controller responsible for managing schedules.
 *
 * <p>Provides endpoints to retrieve, update and delete schedules.</p>
 */
@RestController
@RequestMapping(SCHEDULE)
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * Retrieves all available schedules.
     *
     * @return a list of {@link ScheduleDTO} representing all schedules
     */
    @GetMapping
    public ResponseEntity<List<ScheduleDTO>> getAll() {
        return ResponseEntity.ok(scheduleService.findAll());
    }

    /**
     * Retrieves a schedule for a specific day of the week.
     *
     * @param dayOfWeek the day of the week
     * @return the schedule data as {@link ScheduleDTO}
     */
    @GetMapping(ID)
    public ResponseEntity<ScheduleDTO> getById(
            @PathVariable(name = "id") DayOfWeek dayOfWeek) {
        return ResponseEntity.ok(scheduleService.findSchedule(dayOfWeek));
    }

    /**
     * Updates one or more schedules.
     *
     * @param writes a set of schedules to be updated
     * @return no content if the update is successful
     */
    @PutMapping()
    public ResponseEntity<Void> update(
            @RequestBody Set<ScheduleWrite> writes) {
        scheduleService.update(writes);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes a schedule by its unique identifier.
     *
     * @param id the UUID of the schedule to delete
     * @return no content if the deletion is successful
     */
    @DeleteMapping(ID)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
