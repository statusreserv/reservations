package com.statusreserv.reservations.service.service;

import com.statusreserv.reservations.dto.service.ServiceProvidedDTO;
import com.statusreserv.reservations.dto.service.ServiceProvidedWrite;
import com.statusreserv.reservations.model.service.ServiceProvided;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ServiceProvidedService {

    List<ServiceProvidedDTO> findAll();

    ServiceProvidedDTO findService(UUID id);

    ServiceProvided findById(UUID id);

    UUID create(ServiceProvidedWrite write);

    void update(UUID id, ServiceProvidedWrite write);

    void delete(UUID id);

}
