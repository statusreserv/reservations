package com.statusreserv.reservations.service.auth;

import com.statusreserv.reservations.config.security.TokenService;
import com.statusreserv.reservations.dto.auth.AuthenticationDTO;
import com.statusreserv.reservations.dto.auth.LoginResponseDTO;
import com.statusreserv.reservations.model.user.UserAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public LoginResponseDTO login(AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        return new LoginResponseDTO(tokenService.generateToken((UserAuth) auth.getPrincipal()));
    }
}
