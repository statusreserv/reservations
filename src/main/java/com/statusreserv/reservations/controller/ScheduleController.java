package com.statusreserv.reservations.controller;

import com.statusreserv.reservations.dto.ScheduleDto;
import com.statusreserv.reservations.dto.ScheduleWrite;
import com.statusreserv.reservations.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<List<ScheduleDto>> getAll() {
        return ResponseEntity.ok(scheduleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(scheduleService.findSchedule(id));
    }

    @PostMapping
    public ResponseEntity<UUID> create(@RequestBody ScheduleWrite write) {
        return ResponseEntity.ok(scheduleService.create(write));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @RequestBody ScheduleWrite write) {
        scheduleService.update(id,write);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
