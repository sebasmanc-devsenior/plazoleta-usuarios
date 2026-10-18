package com.plazoleta.users.application.port.input;

import com.plazoleta.users.domain.model.UserAccount;

/** Consulta de un usuario por id (la usa Plazoleta para validar roles). */
public interface GetUserUseCase {
    UserAccount execute(long userId);
}
