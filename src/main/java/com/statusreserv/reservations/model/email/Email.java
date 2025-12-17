package com.statusreserv.reservations.model.email;

import com.statusreserv.reservations.model.constants.EmailStatus;
import com.statusreserv.reservations.model.tenant.Tenant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@With
@Table(name = "tbl_emails")
public class Email {

    @Id
    @GeneratedValue
    private UUID id;

    private String recipient;

    private String sender;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private int attempts;

    private LocalDateTime scheduledTime;

    @Enumerated(EnumType.STRING)
    private EmailStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tenant tenant;

}