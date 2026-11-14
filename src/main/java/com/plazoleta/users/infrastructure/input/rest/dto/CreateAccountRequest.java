package com.plazoleta.users.infrastructure.input.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.plazoleta.users.application.dto.NewAccountCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateAccountRequest(
        @NotBlank(message = "El nombre es obligatorio") @Size(max = 100, message = "El nombre puede tener máximo 100 caracteres") String name,
        @NotBlank(message = "El apellido es obligatorio") @Size(max = 100, message = "El apellido puede tener máximo 100 caracteres") String lastName,
        @NotBlank(message = "El documento de identidad es obligatorio") @Pattern(regexp = "\\d+", message = "El documento de identidad solo puede tener dígitos") @Size(max = 40, message = "El documento de identidad puede tener máximo 40 caracteres") String identityDocument,
        @Size(max = 13, message = "El celular debe tener máximo 13 caracteres") @Pattern(regexp = "^$|^(?:\\+\\d{7,12}|\\d{7,13})$", message = "El formato del celular no es válido") String phone,
        @PastOrPresent(message = "La fecha de nacimiento no puede ser futura") LocalDate birthDate,
        @NotBlank(message = "El correo es obligatorio") @Email(message = "El formato del correo no es válido") @Size(max = 254, message = "El correo puede tener máximo 254 caracteres") String email,
        @NotBlank(message = "La clave es obligatoria") @Size(min = 8, max = 72, message = "La clave debe tener entre 8 y 72 caracteres") String password,
        @Positive(message = "El restaurantId debe ser positivo") Long restaurantId) {

    public NewAccountCommand toCommand() {
        return new NewAccountCommand(name, lastName, identityDocument, phone, birthDate, email, password);
    }
}
