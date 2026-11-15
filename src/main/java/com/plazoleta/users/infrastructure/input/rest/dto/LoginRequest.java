package com.plazoleta.users.infrastructure.input.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "El correo es obligatorio") @Email(message = "El formato del correo no es válido") @Size(max = 254, message = "El correo puede tener máximo 254 caracteres") String email,
        @NotBlank(message = "La clave es obligatoria") @Size(max = 72, message = "La clave puede tener máximo 72 caracteres") String password) {}
