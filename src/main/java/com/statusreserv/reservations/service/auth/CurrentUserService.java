package com.statusreserv.reservations.service.auth;

import com.statusreserv.reservations.model.tenant.Tenant;
import com.statusreserv.reservations.model.user.UserAuth;
import com.statusreserv.reservations.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    public UserAuth getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
            throw new UsernameNotFoundException("No authenticated user found");
        }

        if (!(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            throw new UsernameNotFoundException("Invalid authenticated principal");
        }

        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userDetails.getUsername()));
    }

    public UUID getCurrentTenantId() {
        return getCurrentUser().getTenant().getId();
    }

    public Tenant getCurrentTenant() {
        return getCurrentUser().getTenant();
    }
}
