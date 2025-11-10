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

@RestController
@RequestMapping(SCHEDULE)
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<List<ScheduleDTO>> getAll() {
        return ResponseEntity.ok(scheduleService.findAll());
    }

    @GetMapping(ID)
    public ResponseEntity<ScheduleDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(scheduleService.findSchedule(id));
    }

    @PostMapping
    public ResponseEntity<UUID> create(@RequestBody ScheduleWrite write) {
        return ResponseEntity.ok(scheduleService.create(write));
    }

    @PutMapping(ID)
    public ResponseEntity<Void> update(@PathVariable UUID id, @RequestBody ScheduleWrite write) {
        scheduleService.update(id,write);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(ID)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
