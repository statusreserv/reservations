package com.statusreserv.reservations.controller;

import com.statusreserv.reservations.dto.auth.AuthenticationDTO;
import com.statusreserv.reservations.service.auth.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.statusreserv.reservations.constants.Endpoints.AUTH;
import static com.statusreserv.reservations.constants.Endpoints.LOGIN;

@RestController
@RequestMapping(AUTH)
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class LoginController {

    private final LoginService loginService;

    /**
     * Authenticates a user and returns a JWT token along with user info.
     *
     * @param data AuthenticationDTO containing username and password
     * @return AuthenticationResponseDTO with JWT token and user details
     */
    @PostMapping(LOGIN)
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data) {
        return ResponseEntity.ok(loginService.login(data));
    }
}
