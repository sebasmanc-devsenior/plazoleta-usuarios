package com.plazoleta.users.application.usecase;

import com.plazoleta.users.application.dto.NewAccountCommand;
import com.plazoleta.users.application.port.input.BootstrapAdminPort;
import com.plazoleta.users.application.port.output.UserAccountRepositoryPort;
import com.plazoleta.users.domain.model.UserRole;

public class BootstrapAdminUseCase implements BootstrapAdminPort {
    private final UserAccountRepositoryPort accounts;
    private final AccountRegistrar registrar;

    public BootstrapAdminUseCase(UserAccountRepositoryPort accounts, AccountRegistrar registrar) {
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
