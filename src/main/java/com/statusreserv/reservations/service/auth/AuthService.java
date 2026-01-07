package com.statusreserv.reservations.service.auth;

import com.statusreserv.reservations.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service responsible for authentication and user lookup.
 *
 * <p>Implements {@link UserDetailsService} to provide user details
 * to Spring Security during authentication.
 */
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads a user by their username (email) for authentication.
     *
     * @param username the username (email) of the user
     * @return a {@link UserDetails} object representing the user
     * @throws UsernameNotFoundException if no user is found with the given username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
