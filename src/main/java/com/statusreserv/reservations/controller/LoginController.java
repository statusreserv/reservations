package com.statusreserv.reservations.controller;

import com.statusreserv.reservations.dto.auth.AuthenticationDTO;
import com.statusreserv.reservations.service.auth.LoginService;
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
public class LoginController {
    private final LoginService loginService;

    @PostMapping(LOGIN)
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data) {
        return ResponseEntity.ok(loginService.login(data));
    }

}
