package com.statusreserv.reservations.model.customer;

import com.statusreserv.reservations.model.reservation.Reservation;
import com.statusreserv.reservations.model.tenant.Tenant;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@With
@Table(name = "tbl_customers")
public class Customer {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String email;
    private String phone;
    private Instant createdAt;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tenant tenant;
}
