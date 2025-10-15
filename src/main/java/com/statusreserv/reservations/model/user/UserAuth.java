package com.statusreserv.reservations.model.user;

import com.statusreserv.reservations.model.tenant.Tenant;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@With
@Table(name = "tbl_user_auth")
public class UserAuth {
    @Id
    @GeneratedValue
    private UUID id;

    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tenant tenant;

    @OneToOne(cascade = CascadeType.ALL)
    private UserProfile userProfile;
}
