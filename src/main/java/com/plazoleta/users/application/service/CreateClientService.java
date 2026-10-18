package com.plazoleta.users.application.service;

import com.plazoleta.users.application.dto.NewAccountCommand;
import com.plazoleta.users.application.port.input.CreateClientUseCase;
import com.plazoleta.users.domain.model.UserAccount;
import com.plazoleta.users.domain.model.UserRole;

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
