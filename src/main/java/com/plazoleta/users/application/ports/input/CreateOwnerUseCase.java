package com.plazoleta.users.application.ports.input;

import com.plazoleta.users.application.dto.NewAccountCommand;
import com.plazoleta.users.domain.models.UserAccount;

/** HU-1: el administrador crea la cuenta de un propietario. */
public interface CreateOwnerUseCase {
    UserAccount execute(NewAccountCommand command);
}
