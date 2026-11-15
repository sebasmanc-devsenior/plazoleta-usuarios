package com.plazoleta.users.application.handler;

import com.plazoleta.users.application.dto.LoginResult;
import com.plazoleta.users.application.dto.NewAccountCommand;
import com.plazoleta.users.domain.model.UserAccount;

/**
 * Handler de usuarios: lo que los controladores le piden a la capa de aplicación (HU 1, 5, 6 y 8, y la consulta
 * interna). No tiene reglas de negocio, solo delega en los casos de uso por sus interfaces (puertos de entrada).
 */
public interface IUserHandler {
    UserAccount createOwner(NewAccountCommand command);

    LoginResult login(String email, String password);

    UserAccount createEmployee(NewAccountCommand command, Long restaurantId, long requesterId);

    UserAccount createClient(NewAccountCommand command);

    UserAccount getUser(long userId);
}
