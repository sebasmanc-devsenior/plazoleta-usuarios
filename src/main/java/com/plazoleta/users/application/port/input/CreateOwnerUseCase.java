package com.plazoleta.users.application.port.input;

import com.plazoleta.users.application.dto.NewAccountCommand;
import com.plazoleta.users.domain.model.UserAccount;

/** HU-1: el administrador crea la cuenta de un propietario. */
public interface CreateOwnerUseCase {
    UserAccount execute(NewAccountCommand command);
}
