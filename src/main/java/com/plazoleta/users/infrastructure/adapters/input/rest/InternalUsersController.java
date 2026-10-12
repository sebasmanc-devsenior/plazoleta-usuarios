package com.plazoleta.users.infrastructure.adapters.input.rest;

import com.plazoleta.users.application.ports.input.GetUserUseCase;
import com.plazoleta.users.infrastructure.adapters.input.rest.dto.InternalUserResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/** Consulta entre microservicios (token interno). Oculta en Swagger porque no es API pública. */
@Hidden
@RestController
@RequestMapping("/internal/users")
public class InternalUsersController {
    private final GetUserUseCase getUserUseCase;
    private final String internalToken;

    public InternalUsersController(GetUserUseCase getUserUseCase,
                                  @Value("${app.internal.token}") String internalToken) {
        this.getUserUseCase = getUserUseCase;
        this.internalToken = internalToken;
    }

    @GetMapping("/{userId}")
    public InternalUserResponse getUser(@PathVariable long userId, @RequestHeader(name = "Authorization", required = false) String authorization) {
        if (internalToken == null || internalToken.isBlank()) {
            throw new InternalServiceAuthException(HttpStatus.SERVICE_UNAVAILABLE, "Service authentication is not configured");
        }
        String supplied = authorization != null && authorization.startsWith("Bearer ")
                ? authorization.substring(7) : "";
        if (!MessageDigest.isEqual(internalToken.getBytes(StandardCharsets.UTF_8),
                supplied.getBytes(StandardCharsets.UTF_8))) {
            throw new InternalServiceAuthException(HttpStatus.UNAUTHORIZED, "Invalid service credentials");
        }
        return InternalUserResponse.from(getUserUseCase.execute(userId));
    }

    public static class InternalServiceAuthException extends RuntimeException {
        private final HttpStatus status;
        InternalServiceAuthException(HttpStatus status, String message) {
            super(message);
            this.status = status;
        }
        public HttpStatus status() { return status; }
    }
}
