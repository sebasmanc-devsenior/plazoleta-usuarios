package com.plazoleta.users.application.service;

import com.plazoleta.users.application.dto.NewAccountCommand;
import com.plazoleta.users.application.port.input.BootstrapAdminUseCase;
import com.plazoleta.users.application.port.output.UserAccountRepositoryPort;
import com.plazoleta.users.domain.model.UserRole;

public class BootstrapAdminService implements BootstrapAdminUseCase {
    private final UserAccountRepositoryPort accounts;
    private final AccountRegistrar registrar;

    public BootstrapAdminService(UserAccountRepositoryPort accounts, AccountRegistrar registrar) {
        this.accounts = accounts;
        this.registrar = registrar;
    }

    @Override
    public void execute(NewAccountCommand command) {
        if (!accounts.existsByEmail(AccountRegistrar.normalizeEmail(command.email()))) {
            registrar.register(command, UserRole.ADMIN, null);
        }
    }
}
