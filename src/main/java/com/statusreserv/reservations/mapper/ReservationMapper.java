package com.statusreserv.reservations.mapper;

import com.statusreserv.reservations.dto.reservation.ReservationDTO;
import com.statusreserv.reservations.dto.reservation.ReservationWrite;
import com.statusreserv.reservations.model.reservation.Reservation;
import com.statusreserv.reservations.model.reservation.ReservationServiceProvided;
import com.statusreserv.reservations.model.tenant.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    default UUID map(ReservationServiceProvided rsp) {
        return rsp.getServiceProvidedId();
    }

    @Mapping(target = "reservationServices", source = "reservationServices")
    ReservationDTO toDTO(Reservation reservation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reservationServices", ignore = true)
    @Mapping(target = "tenant", source = "tenant")
    Reservation toEntity(ReservationWrite dto, Tenant tenant);

}
