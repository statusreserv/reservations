package com.statusreserv.reservations.controller;

import com.statusreserv.reservations.dto.schedule.ScheduleDTO;
import com.statusreserv.reservations.dto.schedule.ScheduleWrite;
import com.statusreserv.reservations.service.schedule.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.statusreserv.reservations.constants.Endpoints.ID;
import static com.statusreserv.reservations.constants.Endpoints.SCHEDULE;

/**
 * REST controller for managing schedules.
 *
 * <p>Provides endpoints to create, update, delete, and retrieve schedules.
 */
@RestController
@RequestMapping(SCHEDULE)
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * Retrieves all schedules.
     *
     * @return a list of {@link ScheduleDTO} representing all schedules
     */
    @GetMapping
    public ResponseEntity<List<ScheduleDTO>> getAll() {
        return ResponseEntity.ok(scheduleService.findAll());
    }

    /**
     * Retrieves a schedule by its unique identifier.
     *
     * @param id the UUID of the schedule
     * @return the schedule data as {@link ScheduleDTO}
     *
     */
    @GetMapping(ID)
    public ResponseEntity<ScheduleDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(scheduleService.findSchedule(id));
    }

    /**
     * Creates a new schedule.
     *
     * @param write the schedule data to create
     * @return the UUID of the newly created schedule
     */
    @PostMapping
    public ResponseEntity<UUID> create(@RequestBody ScheduleWrite write) {
        return ResponseEntity.ok(scheduleService.create(write));
    }

    /**
     * Updates an existing schedule.
     *
     * @param id the UUID of the schedule to update
     * @param write the new schedule data
     * @return no content
     *
     */
    @PutMapping(ID)
    public ResponseEntity<Void> update(@PathVariable UUID id, @RequestBody ScheduleWrite write) {
        scheduleService.update(id, write);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes a schedule by its unique identifier.
     *
     * @param id the UUID of the schedule to delete
     * @return no content
     *
     */
    @DeleteMapping(ID)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
