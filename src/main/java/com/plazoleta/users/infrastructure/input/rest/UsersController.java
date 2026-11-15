package com.plazoleta.users.infrastructure.input.rest;

import com.plazoleta.users.domain.util.MensajesError;

import com.plazoleta.users.application.handler.IUserHandler;
import com.plazoleta.users.infrastructure.input.rest.dto.AccountResponse;
import com.plazoleta.users.infrastructure.input.rest.dto.CreateAccountRequest;
import com.plazoleta.users.infrastructure.input.rest.dto.EmployeeAccountResponse;
import com.plazoleta.users.infrastructure.input.rest.dto.LoginRequest;
import com.plazoleta.users.infrastructure.input.rest.dto.LoginResponse;
import com.plazoleta.users.infrastructure.input.rest.mapper.AccountRequestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Users", description = "Accounts, roles and authentication")
public class UsersController {
    private final IUserHandler userHandler;

    public UsersController(IUserHandler userHandler) {
        this.userHandler = userHandler;
    }

    @PostMapping("/owners")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "HU-1 Create owner", description = "ADMIN only. The user must be at least 18 years old.")
    public AccountResponse createOwner(@Valid @RequestBody CreateAccountRequest request) {
        requireNoRestaurant(request);
        return AccountResponse.from(userHandler.createOwner(AccountRequestMapper.toCommand(request)));
    }

    @PostMapping("/login")
    @Operation(summary = "HU-5 Log in", description = "Returns the JWT and the user's id, name, last name, email and role.")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        var result = userHandler.login(request.email(), request.password());
        return new LoginResponse(result.token(), result.id(), result.name(), result.lastName(), result.email(),
                result.role());
    }

    @PostMapping("/employees")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "HU-6 Create employee", description = "PROPIETARIO only, and only for a restaurant they own.")
    public EmployeeAccountResponse createEmployee(@Valid @RequestBody CreateAccountRequest request,
                                                  Authentication authentication) {
        // El id del propietario sale del JWT (subject), nunca del cuerpo de la petición.
        long requesterId = Long.parseLong(authentication.getName());
        return EmployeeAccountResponse.from(
                userHandler.createEmployee(AccountRequestMapper.toCommand(request), request.restaurantId(), requesterId));
    }

    @PostMapping("/clients")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "HU-8 Create client account", description = "Public sign-up; the account gets the CLIENTE role.")
    public AccountResponse createClient(@Valid @RequestBody CreateAccountRequest request) {
        requireNoRestaurant(request);
        return AccountResponse.from(userHandler.createClient(AccountRequestMapper.toCommand(request)));
    }

    private void requireNoRestaurant(CreateAccountRequest request) {
        if (request.restaurantId() != null) {
            throw new IllegalArgumentException(MensajesError.RESTAURANTID_NO_SE_PERMITE_PARA_ESTE_TIPO);
        }
    }
}
