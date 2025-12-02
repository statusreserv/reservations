package com.statusreserv.reservations.service.service;

import com.statusreserv.reservations.dto.service.ServiceProvidedDTO;
import com.statusreserv.reservations.dto.service.ServiceProvidedWrite;
import com.statusreserv.reservations.repository.service.ServiceProvided;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public interface ServiceProvidedService {

    List<ServiceProvidedDTO> findAll();

    ServiceProvidedDTO findService(UUID id);

    ServiceProvided findById(UUID id);

    List<ServiceProvided> findByIdIn(Set<UUID> id);

    UUID create(ServiceProvidedWrite write);

    void update(UUID id, ServiceProvidedWrite write);

    void delete(UUID id);

}
