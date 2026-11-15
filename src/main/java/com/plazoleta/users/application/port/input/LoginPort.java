package com.plazoleta.users.application.port.input;

import com.plazoleta.users.application.dto.LoginResult;

/** HU-5: inicio de sesión con correo y clave. */
public interface LoginPort {
    LoginResult execute(String email, String password);
}
