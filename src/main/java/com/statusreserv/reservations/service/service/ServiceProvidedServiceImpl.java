package com.statusreserv.reservations.service.service;

import com.statusreserv.reservations.dto.service.ServiceProvidedDTO;
import com.statusreserv.reservations.dto.service.ServiceProvidedWrite;
import com.statusreserv.reservations.mapper.ServiceProvidedMapper;
import com.statusreserv.reservations.model.service.ServiceProvided;
import com.statusreserv.reservations.repository.ServiceProvidedRepository;
import com.statusreserv.reservations.service.auth.CurrentUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceProvidedServiceImpl implements ServiceProvidedService {

    private final ServiceProvidedRepository repository;
    private final ServiceProvidedMapper mapper;
    private final CurrentUserService currentUserService;

    public List<ServiceProvidedDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public ServiceProvidedDTO findService(UUID id) {
        return mapper.toDTO(findById(id));
    }

    public ServiceProvided findById(UUID id) {
        return repository.findByIdAndTenantId(id, currentUserService.getCurrentTenantId())
                .orElseThrow(() -> new EntityNotFoundException("Tenant not found"));
    }

    @Transactional
    public UUID create(ServiceProvidedWrite write) {
        var service = mapper.toEntity(write, currentUserService.getCurrentTenant());
        return repository.save(service).getId();
    }

    @Transactional
    public void update(UUID id, ServiceProvidedWrite write) {
        var existing = findById(id);

        existing.setName(write.name());
        existing.setDescription(write.description());
        existing.setPrice(write.price());
        existing.setDurationMinutes(write.durationMinutes());

        repository.save(existing);
    }

    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Tenant not found");
        }
        repository.deleteById(id);
    }
}
