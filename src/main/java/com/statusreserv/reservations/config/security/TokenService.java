package com.statusreserv.reservations.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.statusreserv.reservations.model.user.UserAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Service responsible for generating and validating JWT tokens.
 */
@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Generates a JWT token for the given user.
     *
     * @param user UserAuth object containing the username
     * @return JWT token as a String
     */
    public String generateToken(UserAuth user) {
        try {
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getUsername())
                    .withExpiresAt(getExpiration())
                    .sign(Algorithm.HMAC256(secret));
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error while generating token", e);
        }
    }

    /**
     * Validates a JWT token and returns the username if valid.
     *
     * @param token JWT token to validate
     * @return username if token is valid, empty string otherwise
     */
    public String validateToken(String token) {
        try {
            var  algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return "";
        }
    }

    private Instant getExpiration() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.UTC);
    }
}
