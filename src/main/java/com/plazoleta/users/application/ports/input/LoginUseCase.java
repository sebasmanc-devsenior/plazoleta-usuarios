package com.plazoleta.users.application.ports.input;

import com.plazoleta.users.application.dto.LoginResult;

/** HU-5: inicio de sesión con correo y clave. */
public interface LoginUseCase {
    LoginResult execute(String email, String password);
}
