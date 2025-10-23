package com.statusreserv.reservations.repository;

import com.statusreserv.reservations.model.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findByTenantId(UUID tenantId);
    List<Reservation> findByTenantIdAndDate(UUID tenantId, LocalDate date);
    Optional<Reservation> findByIdAndTenantId(UUID id, UUID tenantId);
    void deleteByIdAndTenantId(UUID id, UUID tenantId);

}
