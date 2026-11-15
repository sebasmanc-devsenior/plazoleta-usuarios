package com.plazoleta.users.infrastructure.input.rest.dto;

/** HU-5: respuesta del login: token, id, nombre, apellido, correo y rol (nunca la clave). */
public record LoginResponse(String token, Long id, String name, String lastName, String email, String role) {}
