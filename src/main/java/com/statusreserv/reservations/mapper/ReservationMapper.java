package com.statusreserv.reservations.mapper;

import com.statusreserv.reservations.dto.reservation.ReservationDTO;
import com.statusreserv.reservations.dto.reservation.ReservationServiceProvidedDTO;
import com.statusreserv.reservations.dto.reservation.ReservationServiceProvidedWrite;
import com.statusreserv.reservations.dto.reservation.ReservationWrite;
import com.statusreserv.reservations.model.reservation.Reservation;
import com.statusreserv.reservations.model.reservation.ReservationServiceProvided;
import com.statusreserv.reservations.model.tenant.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "reservationService", source = "serviceReservation")
    ReservationDTO toDTO(Reservation reservation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenant", source = "tenant")
    Reservation toEntity(ReservationWrite dto, Tenant tenant);

    ReservationServiceProvidedDTO toDTO(ReservationServiceProvided reservationServiceProvided);
    ReservationServiceProvided toEntity(ReservationServiceProvidedWrite dto);
}
