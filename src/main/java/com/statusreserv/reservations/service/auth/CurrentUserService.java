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

/**
 * Service responsible for retrieving the currently authenticated user and their tenant.
 *
 * <p>Uses Spring Security's {@link SecurityContextHolder} to access authentication information.
 */
@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    /**
     * Retrieves the currently authenticated user.
     *
     * @return the authenticated {@link UserAuth} entity
     * @throws UsernameNotFoundException if no authenticated user is found or the principal is invalid
     */
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

    /**
     * Retrieves the UUID of the tenant associated with the currently authenticated user.
     *
     * @return the tenant's UUID
     */
    public UUID getCurrentTenantId() {
        return getCurrentUser().getTenant().getId();
    }

    /**
     * Retrieves the tenant entity associated with the currently authenticated user.
     *
     * @return the {@link Tenant} entity
     */
    public Tenant getCurrentTenant() {
        return getCurrentUser().getTenant();
    }
}
