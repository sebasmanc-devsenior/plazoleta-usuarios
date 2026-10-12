package com.plazoleta.users.application.ports.input;

import com.plazoleta.users.application.dto.NewAccountCommand;

/** Crea el administrador inicial si todavía no existe. */
public interface BootstrapAdminUseCase {
    void execute(NewAccountCommand command);
}
