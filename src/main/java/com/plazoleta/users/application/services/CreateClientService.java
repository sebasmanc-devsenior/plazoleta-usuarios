package com.plazoleta.users.application.services;

import com.plazoleta.users.application.dto.NewAccountCommand;
import com.plazoleta.users.application.ports.input.CreateClientUseCase;
import com.plazoleta.users.domain.models.UserAccount;
import com.plazoleta.users.domain.models.UserRole;

/** HU-8: registro abierto; la cuenta siempre queda con rol CLIENTE. */
public class CreateClientService implements CreateClientUseCase {
    private final AccountRegistrar registrar;

    public CreateClientService(AccountRegistrar registrar) {
        this.registrar = registrar;
    }

    @Override
    public UserAccount execute(NewAccountCommand command) {
        return registrar.register(command, UserRole.CLIENTE, null);
    }
}
