package com.statusreserv.reservations.repository;

import com.statusreserv.reservations.model.user.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserAuth, UUID> {
    Optional<UserAuth> findByEmail(String email);
}
