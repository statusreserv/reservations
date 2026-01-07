package com.statusreserv.reservations.service.service;

import com.statusreserv.reservations.dto.service.ServiceProvidedDTO;
import com.statusreserv.reservations.dto.service.ServiceProvidedWrite;
import com.statusreserv.reservations.repository.service.ServiceProvided;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Service interface responsible for managing services provided by the tenant.
 *
 * <p>Supports operations such as creating, retrieving, updating, and deleting services.
 */
@Service
public interface ServiceProvidedService {

    /**
     * Retrieves all services for the current tenant.
     *
     * @return a list of {@link ServiceProvidedDTO} representing all services
     */
    List<ServiceProvidedDTO> findAll();

    /**
     * Retrieves a service by its unique identifier and maps it to DTO.
     *
     * @param id the UUID of the service
     * @return a {@link ServiceProvidedDTO} representing the service
     *
     */
    ServiceProvidedDTO findService(UUID id);

    /**
     * Retrieves the entity of a service by its unique identifier.
     *
     * @param id the UUID of the service
     * @return the {@link ServiceProvided} entity
     *
     */
    ServiceProvided findById(UUID id);

    /**
     * Retrieves a list of service entities by their identifiers.
     *
     * @param id set of UUIDs of the services
     * @return a list of {@link ServiceProvided} entities
     */
    List<ServiceProvided> findByIdIn(Set<UUID> id);

    /**
     * Creates a new service.
     *
     * @param write DTO containing service data to create
     * @return the UUID of the newly created service
     */
    UUID create(ServiceProvidedWrite write);

    /**
     * Updates an existing service.
     *
     * @param id the UUID of the service to update
     * @param write DTO containing new service data
     *
     */
    void update(UUID id, ServiceProvidedWrite write);

    /**
     * Deletes a service by its unique identifier.
     *
     * @param id the UUID of the service to delete
     *
     */
    void delete(UUID id);
}
