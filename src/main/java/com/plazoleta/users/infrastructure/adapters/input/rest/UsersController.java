package com.plazoleta.users.infrastructure.adapters.input.rest;

import com.plazoleta.users.application.ports.input.CreateOwnerUseCase;
import com.plazoleta.users.application.ports.input.LoginUseCase;
import com.plazoleta.users.infrastructure.adapters.input.rest.dto.AccountResponse;
import com.plazoleta.users.infrastructure.adapters.input.rest.dto.CreateAccountRequest;
import com.plazoleta.users.infrastructure.adapters.input.rest.dto.LoginRequest;
import com.plazoleta.users.infrastructure.adapters.input.rest.dto.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Users", description = "Accounts, roles and authentication")
public class UsersController {
    private final CreateOwnerUseCase createOwnerUseCase;
    private final LoginUseCase loginUseCase;

    public UsersController(CreateOwnerUseCase createOwnerUseCase, LoginUseCase loginUseCase) {
        this.createOwnerUseCase = createOwnerUseCase;
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/owners")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "HU-1 Create owner", description = "ADMIN only. The user must be at least 18 years old.")
    public AccountResponse createOwner(@Valid @RequestBody CreateAccountRequest request) {
        requireNoRestaurant(request);
        return AccountResponse.from(createOwnerUseCase.execute(request.toCommand()));
    }

    @PostMapping("/login")
    @Operation(summary = "HU-5 Log in", description = "Returns a JWT with the user's id and role.")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        var result = loginUseCase.execute(request.email(), request.password());
        return new LoginResponse(result.token(), result.userId(), result.role());
    }

    private void requireNoRestaurant(CreateAccountRequest request) {
        if (request.restaurantId() != null) {
            throw new IllegalArgumentException("restaurantId is not allowed for this account role");
        }
    }
}
