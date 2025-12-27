package com.statusreserv.reservations.service.service;

import com.statusreserv.reservations.dto.service.ServiceProvidedDTO;
import com.statusreserv.reservations.dto.service.ServiceProvidedWrite;
import com.statusreserv.reservations.mapper.ServiceProvidedMapper;
import com.statusreserv.reservations.repository.service.ServiceProvided;
import com.statusreserv.reservations.repository.ServiceProvidedRepository;
import com.statusreserv.reservations.service.auth.CurrentUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of {@link ServiceProvidedService} responsible for managing
 * tenant services, including CRUD operations.
 *
 * <p>Handles mapping between DTOs and entities, tenant scoping, and persistence.
 */
@Service
@RequiredArgsConstructor
public class ServiceProvidedServiceImpl implements ServiceProvidedService {

    private final ServiceProvidedRepository repository;
    private final ServiceProvidedMapper mapper;
    private final CurrentUserService currentUserService;

    /**
     * Retrieves all services for the current tenant.
     *
     * @return a list of {@link ServiceProvidedDTO} representing all services
     */
    public List<ServiceProvidedDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a service by its UUID and maps it to DTO.
     *
     * @param id the UUID of the service
     * @return a {@link ServiceProvidedDTO} representing the service
     * @throws EntityNotFoundException if no service exists with the given id
     */
    public ServiceProvidedDTO findService(UUID id) {
        return mapper.toDTO(findById(id));
    }

    /**
     * Retrieves a service entity by its UUID for the current tenant.
     *
     * @param id the UUID of the service
     * @return the {@link ServiceProvided} entity
     * @throws EntityNotFoundException if no service exists with the given id
     */
    public ServiceProvided findById(UUID id) {
        return repository.findByIdAndTenantId(id, currentUserService.getCurrentTenantId())
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));
    }

    /**
     * Retrieves a list of service entities by a set of UUIDs.
     *
     * @param ids the set of service UUIDs
     * @return a list of {@link ServiceProvided} entities
     */
    public List<ServiceProvided> findByIdIn(Set<UUID> ids) {
        return repository.findByIdInAndTenantId(ids, currentUserService.getCurrentTenantId());
    }

    /**
     * Creates a new service for the current tenant.
     *
     * @param write DTO containing service data
     * @return the UUID of the newly created service
     */
    @Transactional
    public UUID create(ServiceProvidedWrite write) {
        var service = mapper.toEntity(write, currentUserService.getCurrentTenant());
        return repository.save(service).getId();
    }

    /**
     * Updates an existing service with new data.
     *
     * @param id the UUID of the service to update
     * @param write DTO containing updated service data
     * @throws EntityNotFoundException if no service exists with the given id
     */
    @Transactional
    public void update(UUID id, ServiceProvidedWrite write) {
        var existing = findById(id);

        existing.setName(write.name());
        existing.setDescription(write.description());
        existing.setPrice(write.price());
        existing.setDurationMinutes(write.durationMinutes());

        repository.save(existing);
    }

    /**
     * Deletes a service by its UUID.
     *
     * @param id the UUID of the service to delete
     * @throws EntityNotFoundException if no service exists with the given id
     */
    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Service not found");
        }
        repository.deleteById(id);
    }
}
