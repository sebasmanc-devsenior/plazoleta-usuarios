package com.plazoleta.users.application.services;

import com.plazoleta.users.application.dto.NewAccountCommand;
import com.plazoleta.users.application.ports.input.CreateOwnerUseCase;
import com.plazoleta.users.domain.models.UserAccount;
import com.plazoleta.users.domain.models.UserRole;

/** HU-1: la cuenta queda con rol PROPIETARIO; el dominio exige que sea mayor de edad. */
public class CreateOwnerService implements CreateOwnerUseCase {
    private final AccountRegistrar registrar;

    public CreateOwnerService(AccountRegistrar registrar) {
        this.registrar = registrar;
    }

    @Override
    public UserAccount execute(NewAccountCommand command) {
        return registrar.register(command, UserRole.PROPIETARIO, null);
    }
}
