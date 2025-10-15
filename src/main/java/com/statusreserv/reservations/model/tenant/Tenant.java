package com.statusreserv.reservations.model.tenant;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@With
@Table(name = "tbl_tenant")
public class Tenant {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String phone;
    private String address;
    private String email;
    private Instant createdAt;
    private Instant openYear;
    private boolean enabled;
}
