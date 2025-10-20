package com.statusreserv.reservations.service;

import com.statusreserv.reservations.dto.ServiceProvidedDTO;
import com.statusreserv.reservations.dto.ServiceProvidedWrite;
import com.statusreserv.reservations.mapper.ServiceProvidedMapper;
import com.statusreserv.reservations.model.service.ServiceProvided;
import com.statusreserv.reservations.model.tenant.Tenant;
import com.statusreserv.reservations.repository.ServiceProvidedRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.util.LambdaUtil.getAll;

@Service
@RequiredArgsConstructor
public class ServiceProvidedServiceImpl implements ServiceProvidedService {

    private final ServiceProvidedRepository repository;
    private final ServiceProvidedMapper mapper;

    public List<ServiceProvidedDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public ServiceProvided findByTenantId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tenant not found"));
    }

    public UUID create(ServiceProvidedWrite write) {
        //fica tudo dentro de "service" ou tenho de fzr o resto
        var service = mapper.toEntity(write, new Tenant());// TODO: Temporary placeholder. Replace with the tenant from context to use the actual tenant.

        return repository.save(service).getId();
    }

    public void update(UUID id, ServiceProvidedWrite write) {
        var existing = findByTenantId(id);

        existing.setName(write.name());
        existing.setDescription(write.description());
        existing.setPrice(write.price());
        existing.setDurationMinutes(write.durationMinutes());

        repository.save(existing);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Tenant not found");
        }
        repository.deleteById(id);
    }
}
