package com.plazoleta.users.application.usecase;

import com.plazoleta.users.application.dto.NewAccountCommand;
import com.plazoleta.users.application.port.input.CreateOwnerPort;
import com.plazoleta.users.domain.model.UserAccount;
import com.plazoleta.users.domain.model.UserRole;

/** HU-1: la cuenta queda con rol PROPIETARIO; el dominio exige que sea mayor de edad. */
public class CreateOwnerUseCase implements CreateOwnerPort {
    private final AccountRegistrar registrar;

    public CreateOwnerUseCase(AccountRegistrar registrar) {
        this.registrar = registrar;
    }

    @Override
    public UserAccount execute(NewAccountCommand command) {
        return registrar.register(command, UserRole.PROPIETARIO, null);
    }
}
