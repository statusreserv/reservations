package com.statusreserv.reservations.mapper;

import com.statusreserv.reservations.dto.service.ServiceProvidedDTO;
import com.statusreserv.reservations.dto.service.ServiceProvidedWrite;
import com.statusreserv.reservations.model.service.ServiceProvided;
import com.statusreserv.reservations.model.tenant.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceProvidedMapper {

    ServiceProvidedDTO toDTO(ServiceProvided serviceProvided);

    @Mapping(target = "name", source = "serviceProvidedWrite.name")
    @Mapping(target = "tenant", source = "tenant")
    @Mapping(target = "id", ignore = true)
    ServiceProvided toEntity(ServiceProvidedWrite serviceProvidedWrite, Tenant tenant);
}
