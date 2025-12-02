package com.statusreserv.reservations.model.reservation;

import com.statusreserv.reservations.model.customer.Customer;
import com.statusreserv.reservations.model.tenant.Tenant;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Builder
@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@With
@Table(name = "tbl_reservations")
public class Reservation {
    @Id
    @GeneratedValue
    private UUID id;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Instant createdAt;
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<ReservationServiceProvided> reservationServices;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tenant tenant;
}
