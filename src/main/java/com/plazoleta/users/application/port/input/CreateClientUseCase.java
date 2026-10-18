package com.plazoleta.users.application.port.input;

import com.plazoleta.users.application.dto.NewAccountCommand;
import com.plazoleta.users.domain.model.UserAccount;

/** HU-8: un cliente crea su propia cuenta. */
public interface CreateClientUseCase {
    UserAccount execute(NewAccountCommand command);
}
