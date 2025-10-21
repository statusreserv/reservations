package com.statusreserv.reservations.controller;

import com.statusreserv.reservations.dto.ScheduleDTO;
import com.statusreserv.reservations.dto.ScheduleWrite;
import com.statusreserv.reservations.service.schedule.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleServiceImpl;

    @GetMapping
    public ResponseEntity<List<ScheduleDTO>> getAll() {
        return ResponseEntity.ok(scheduleServiceImpl.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(scheduleServiceImpl.findSchedule(id));
    }

    @PostMapping
    public ResponseEntity<UUID> create(@RequestBody ScheduleWrite write) {
        return ResponseEntity.ok(scheduleServiceImpl.create(write));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @RequestBody ScheduleWrite write) {
        scheduleServiceImpl.update(id,write);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        scheduleServiceImpl.delete(id);
        return ResponseEntity.noContent().build();
    }
}
