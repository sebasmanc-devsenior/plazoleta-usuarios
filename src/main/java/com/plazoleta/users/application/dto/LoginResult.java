package com.plazoleta.users.application.dto;

/** HU-5: resultado del login: el token y los datos de la cuenta (nunca la clave). */
public record LoginResult(String token, Long id, String name, String lastName, String email, String role) {
}
