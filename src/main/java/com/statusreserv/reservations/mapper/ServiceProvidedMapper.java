package com.statusreserv.reservations.mapper;

import com.statusreserv.reservations.dto.ServiceProvidedDTO;
import com.statusreserv.reservations.dto.ServiceProvidedWrite;
import com.statusreserv.reservations.model.service.ServiceProvided;
import com.statusreserv.reservations.model.tenant.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceProvidedMapper {

    ServiceProvidedDTO toDTO(ServiceProvided serviceProvided);

    @Mapping(target = "tenant", source = "tenant")
    ServiceProvided toEntity(ServiceProvidedWrite serviceProvidedWrite, Tenant tenant);
    }
