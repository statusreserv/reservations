package com.statusreserv.reservations.controller;

import com.statusreserv.reservations.dto.service.ServiceProvidedDTO;
import com.statusreserv.reservations.dto.service.ServiceProvidedWrite;
import com.statusreserv.reservations.service.service.ServiceProvidedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceProvidedController {
    private final ServiceProvidedService serviceProvidedService;

    @GetMapping
    public ResponseEntity<List<ServiceProvidedDTO>> findAll() {
        return ResponseEntity.ok(serviceProvidedService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceProvidedDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(serviceProvidedService.findService(id));
    }

    @PostMapping
    private ResponseEntity<UUID> create(@RequestBody ServiceProvidedWrite write) {
        return ResponseEntity.ok(serviceProvidedService.create(write));
    }

    @PutMapping("/{id}")
    private ResponseEntity<Void> update(@PathVariable UUID id, @RequestBody ServiceProvidedWrite write) {
        serviceProvidedService.update(id, write);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> delete(@PathVariable UUID id) {
        serviceProvidedService.delete(id);
        return ResponseEntity.noContent().build();
    }
}