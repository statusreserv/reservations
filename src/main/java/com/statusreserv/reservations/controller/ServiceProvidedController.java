package com.statusreserv.reservations.controller;

import com.statusreserv.reservations.dto.service.ServiceProvidedDTO;
import com.statusreserv.reservations.dto.service.ServiceProvidedWrite;
import com.statusreserv.reservations.service.service.ServiceProvidedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.statusreserv.reservations.constants.Endpoints.*;

/**
 * REST controller responsible for managing tenant services.
 *
 * <p>Provides endpoints to create, retrieve, update, and delete services.
 */
@RestController
@RequestMapping(SERVICES)
@RequiredArgsConstructor
public class ServiceProvidedController {

    private final ServiceProvidedService serviceProvidedService;

    /**
     * Retrieves all services for the current tenant.
     *
     * @return list of {@link ServiceProvidedDTO}
     */
    @GetMapping
    public ResponseEntity<List<ServiceProvidedDTO>> findAll() {
        return ResponseEntity.ok(serviceProvidedService.findAll());
    }

    /**
     * Retrieves a service by its UUID.
     *
     * @param id the UUID of the service
     * @return {@link ServiceProvidedDTO} representing the service
     */
    @GetMapping(ID)
    public ResponseEntity<ServiceProvidedDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(serviceProvidedService.findService(id));
    }

    /**
     * Creates a new service.
     *
     * @param write DTO containing service data
     * @return UUID of the newly created service
     */
    @PostMapping
    public ResponseEntity<UUID> create(@RequestBody ServiceProvidedWrite write) {
        return ResponseEntity.ok(serviceProvidedService.create(write));
    }

    /**
     * Updates an existing service.
     *
     * @param id    UUID of the service to update
     * @param write DTO containing updated service data
     * @return empty response with status 204
     */
    @PutMapping(ID)
    public ResponseEntity<Void> update(@PathVariable UUID id, @RequestBody ServiceProvidedWrite write) {
        serviceProvidedService.update(id, write);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes a service by its UUID.
     *
     * @param id UUID of the service to delete
     * @return empty response with status 204
     */
    @DeleteMapping(ID)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        serviceProvidedService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
