package com.statusreserv.reservations.model.reservation;

import com.statusreserv.reservations.model.customer.Customer;
import com.statusreserv.reservations.model.tenant.Tenant;
import com.statusreserv.reservations.model.user.UserAuth;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

@Entity
@Data
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
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserAuth userAuth;

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<ReservationService> services;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tenant tenant;
}
