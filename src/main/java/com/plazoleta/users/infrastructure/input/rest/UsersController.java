package com.plazoleta.users.infrastructure.input.rest;

import com.plazoleta.users.application.port.input.CreateOwnerUseCase;
import com.plazoleta.users.infrastructure.input.rest.dto.AccountResponse;
import com.plazoleta.users.infrastructure.input.rest.dto.CreateAccountRequest;
import io.swagger.v3.oas.annotations.Operation;
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

    public UsersController(CreateOwnerUseCase createOwnerUseCase) {
        this.createOwnerUseCase = createOwnerUseCase;
    }

    @PostMapping("/owners")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "HU-1 Create owner", description = "ADMIN only. The user must be at least 18 years old.")
    public AccountResponse createOwner(@Valid @RequestBody CreateAccountRequest request) {
        requireNoRestaurant(request);
        return AccountResponse.from(createOwnerUseCase.execute(request.toCommand()));
    }

    private void requireNoRestaurant(CreateAccountRequest request) {
        if (request.restaurantId() != null) {
            throw new IllegalArgumentException("restaurantId is not allowed for this account role");
        }
    }
}
