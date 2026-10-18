package com.plazoleta.users.infrastructure.output.security;

import com.plazoleta.users.application.port.output.TokenProviderPort;
import com.plazoleta.users.domain.model.UserAccount;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

/** Emite el JWT (HS256) que validan todos los microservicios con el secreto compartido. */
@Component
public class JwtTokenProviderAdapter implements TokenProviderPort {
    private final SecretKey key;
    private final Duration expiration;

    public JwtTokenProviderAdapter(@Value("${app.jwt.secret}") String secret,
                                   @Value("${app.jwt.expiration}") Duration expiration) {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("APP_JWT_SECRET (or legacy USERS_JWT_SECRET) must contain at least 32 bytes");
        }
        if (expiration == null || expiration.isZero() || expiration.isNegative()) {
            throw new IllegalStateException("USERS_JWT_EXPIRATION must be a positive duration");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }

    @Override
    public String issue(UserAccount user) {
        Instant now = Instant.now();
        var builder = Jwts.builder()
                .subject(user.id().toString())
                .claim("email", user.email())
                .claim("role", user.role().name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expiration)));
        if (user.restaurantId() != null) {
            builder.claim("restaurantId", user.restaurantId());
        }
        return builder.signWith(key, Jwts.SIG.HS256).compact();
    }
}
