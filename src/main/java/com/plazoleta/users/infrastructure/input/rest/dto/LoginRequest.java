package com.plazoleta.users.infrastructure.input.rest.dto;

import com.plazoleta.users.domain.util.MensajesError;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = MensajesError.CORREO_ES_OBLIGATORIO) @Email(message = MensajesError.FORMATO_DEL_CORREO_NO_ES_VALIDO) @Size(max = 254, message = MensajesError.CORREO_PUEDE_TENER_MAXIMO_254_CARACTERES) String email,
        @NotBlank(message = MensajesError.CLAVE_ES_OBLIGATORIA) @Size(max = 72, message = MensajesError.CLAVE_PUEDE_TENER_MAXIMO_72_CARACTERES) String password) {}
