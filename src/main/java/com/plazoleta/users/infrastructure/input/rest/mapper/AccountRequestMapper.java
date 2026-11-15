package com.plazoleta.users.infrastructure.input.rest.mapper;

import com.plazoleta.users.application.dto.NewAccountCommand;
import com.plazoleta.users.infrastructure.input.rest.dto.CreateAccountRequest;

/** Convierte el request HTTP (ya validado con Jakarta) en el comando de la capa de aplicación. */
public final class AccountRequestMapper {
    private AccountRequestMapper() {
    }

    public static NewAccountCommand toCommand(CreateAccountRequest request) {
        return new NewAccountCommand(request.name(), request.lastName(), request.identityDocument(), request.phone(),
                request.birthDate(), request.email(), request.password());
    }
}
