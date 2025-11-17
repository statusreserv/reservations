package com.statusreserv.reservations.repository;

import com.statusreserv.reservations.model.reservation.Reservation;
import com.statusreserv.reservations.model.reservation.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findByDateBetweenAndStatusInAndTenantId(LocalDate from, LocalDate to, Set<Status> status, UUID tenantId);
    List<Reservation> findByTenantId(UUID tenantId);

    List<Reservation> findByTenantIdAndDate(UUID tenantId, LocalDate date);

    Optional<Reservation> findByIdAndTenantId(UUID id, UUID tenantId);

    void deleteByIdAndTenantId(UUID id, UUID tenantId);
}
