package com.statusreserv.reservations.repository;

import com.statusreserv.reservations.model.tenant.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TenantRepository extends JpaRepository<Tenant, UUID> {
}
