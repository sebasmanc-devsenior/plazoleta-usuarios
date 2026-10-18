package com.plazoleta.users.application.service;

import com.plazoleta.users.application.dto.NewAccountCommand;
import com.plazoleta.users.application.port.input.CreateOwnerUseCase;
import com.plazoleta.users.domain.model.UserAccount;
import com.plazoleta.users.domain.model.UserRole;

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
