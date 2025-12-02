package com.statusreserv.reservations.mapper;

import com.statusreserv.reservations.model.reservation.ReservationServiceProvided;
import com.statusreserv.reservations.repository.service.ServiceProvided;
import com.statusreserv.reservations.model.tenant.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationServiceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenant", source = "tenant")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "reservation", ignore = true)
    @Mapping(target = "serviceProvidedId", source = "serviceProvided.id")
    @Mapping(target = "name", source = "serviceProvided.name")
    ReservationServiceProvided toEntity(ServiceProvided serviceProvided, Tenant tenant);


}