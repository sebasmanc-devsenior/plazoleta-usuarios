package com.plazoleta.users.infrastructure.input.rest.dto;

import com.plazoleta.users.domain.model.AccountRules;
import com.plazoleta.users.domain.util.MensajesError;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateAccountRequest(
        @NotBlank(message = MensajesError.NOMBRE_ES_OBLIGATORIO) @Size(max = 100, message = MensajesError.NOMBRE_PUEDE_TENER_MAXIMO_100_CARACTERES) String name,
        @NotBlank(message = MensajesError.APELLIDO_ES_OBLIGATORIO) @Size(max = 100, message = MensajesError.APELLIDO_PUEDE_TENER_MAXIMO_100_CARACTERES) String lastName,
        @NotBlank(message = MensajesError.DOCUMENTO_DE_IDENTIDAD_ES_OBLIGATORIO) @Pattern(regexp = "\\d+", message = MensajesError.DOCUMENTO_DE_IDENTIDAD_SOLO_PUEDE_TENER_DIGITOS) @Size(max = 40, message = MensajesError.DOCUMENTO_DE_IDENTIDAD_PUEDE_TENER_MAXIMO_40) String identityDocument,
        @Size(max = 13, message = MensajesError.CELULAR_DEBE_TENER_MAXIMO_13_CARACTERES) @Pattern(regexp = "^$|^(?:\\+\\d{7,12}|\\d{7,13})$", message = MensajesError.FORMATO_DEL_CELULAR_NO_ES_VALIDO) String phone,
        @PastOrPresent(message = MensajesError.FECHA_DE_NACIMIENTO_NO_PUEDE_SER_FUTURA) LocalDate birthDate,
        @NotBlank(message = MensajesError.CORREO_ES_OBLIGATORIO) @Email(message = MensajesError.FORMATO_DEL_CORREO_NO_ES_VALIDO) @Size(max = 254, message = MensajesError.CORREO_PUEDE_TENER_MAXIMO_254_CARACTERES) String email,
        @NotBlank(message = MensajesError.CLAVE_ES_OBLIGATORIA) @Size(min = AccountRules.PASSWORD_MIN_LENGTH, max = AccountRules.PASSWORD_MAX_LENGTH, message = MensajesError.CLAVE_DEBE_TENER_ENTRE_6_Y_72) String password,
        @Positive(message = MensajesError.RESTAURANTID_DEBE_SER_POSITIVO) Long restaurantId) {
}
