package com.statusreserv.reservations.service;

import com.statusreserv.reservations.dto.ServiceProvidedDTO;
import com.statusreserv.reservations.dto.ServiceProvidedWrite;
import com.statusreserv.reservations.model.service.ServiceProvided;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ServiceProvidedService {

    List<ServiceProvidedDTO> findAll();

    ServiceProvided findByTenantId(UUID id);

    //why UUID create
    UUID create(ServiceProvidedWrite write);

    void update(UUID id, ServiceProvidedWrite write);

    void delete(UUID id);

}
