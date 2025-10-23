package com.statusreserv.reservations.model.reservation;

import com.statusreserv.reservations.model.tenant.Tenant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@With
@Table(name ="tbl_reservation_services")
public class ReservationServiceProvided {
    @Id
    @GeneratedValue
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
