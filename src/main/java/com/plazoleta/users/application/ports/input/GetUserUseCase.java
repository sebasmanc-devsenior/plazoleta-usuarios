package com.plazoleta.users.application.ports.input;

import com.plazoleta.users.domain.models.UserAccount;

/** Consulta de un usuario por id (la usa Plazoleta para validar roles). */
public interface GetUserUseCase {
    UserAccount execute(long userId);
}
