package com.statusreserv.reservations.repository;

import com.statusreserv.reservations.model.email.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmailRepository extends JpaRepository<Email, UUID> {


    @Query(value = """
        SELECT *
        FROM tbl_emails
        WHERE status = :status
          AND attempts <= :attempts
          AND (scheduled_time IS NULL OR scheduled_time <= NOW())
        FOR UPDATE SKIP LOCKED
        LIMIT 50
        """,
            nativeQuery = true)
    List<Email> fetchEmailsByStatus(
            @Param("status") String status,
            @Param("attempts") int attempts
    );
}
