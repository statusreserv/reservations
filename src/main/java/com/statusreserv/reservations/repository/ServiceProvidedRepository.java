package com.statusreserv.reservations.repository;

import com.statusreserv.reservations.repository.service.ServiceProvided;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ServiceProvidedRepository extends JpaRepository<ServiceProvided, UUID> {
    Optional<ServiceProvided> findByIdAndTenantId(UUID id, UUID tenantId);

    List<ServiceProvided> findByIdInAndTenantId(Set<UUID> id, UUID tenantId);
}
