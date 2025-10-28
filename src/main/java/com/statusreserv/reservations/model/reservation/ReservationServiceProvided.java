package com.statusreserv.reservations.model.reservation;

import com.statusreserv.reservations.model.tenant.Tenant;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@With
@Table(name = "tbl_reservation_services")
public class ReservationServiceProvided {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    private String name;
    private String description;
    private BigDecimal price;
    private Integer durationMinutes;
//    @Enumerated(EnumType.STRING)
    private Instant createdAt;
    private Instant updatedAt;

    private UUID serviceProvidedId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tenant tenant;
}
