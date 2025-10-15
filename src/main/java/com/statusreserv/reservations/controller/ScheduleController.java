package com.statusreserv.reservations.controller;

import com.statusreserv.reservations.dto.ScheduleDto;
import com.statusreserv.reservations.dto.ScheduleWrite;
import com.statusreserv.reservations.service.ScheduleService;
import com.statusreserv.reservations.service.ScheduleServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService service;

    @GetMapping
    public ResponseEntity<List<ScheduleDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findSchedule(id));
    }

    @PostMapping
    public ResponseEntity<UUID> create(@RequestBody ScheduleWrite write) {
        return ResponseEntity.ok(service.create(write));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @RequestBody ScheduleWrite write) {
        service.update(id,write);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
