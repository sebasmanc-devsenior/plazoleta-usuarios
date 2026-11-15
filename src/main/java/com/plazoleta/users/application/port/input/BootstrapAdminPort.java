package com.plazoleta.users.application.port.input;

import com.plazoleta.users.application.dto.NewAccountCommand;

/** Crea el administrador inicial si todavía no existe. */
public interface BootstrapAdminPort {
    void execute(NewAccountCommand command);
}
