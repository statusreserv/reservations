package com.statusreserv.reservations.repository;

import com.statusreserv.reservations.model.service.ServiceProvided;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServiceProvidedRepository extends JpaRepository<ServiceProvided, UUID> {
    List<ServiceProvided> findByTenantId(UUID tenantId);

    Optional<ServiceProvided> findByIdAndTenantId(UUID id, UUID tenantId);
}
