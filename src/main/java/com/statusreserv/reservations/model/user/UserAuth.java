package com.statusreserv.reservations.model.user;

import com.statusreserv.reservations.model.tenant.Tenant;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@With
@Table(name = "tbl_user_auth")
public class UserAuth implements UserDetails {
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return switch (role) {
            case ADMIN -> List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
            case USER -> List.of(new SimpleGrantedAuthority("ROLE_USER"));
        };

    }

    @Override
    public String getUsername() {
        return email;
    }
}
