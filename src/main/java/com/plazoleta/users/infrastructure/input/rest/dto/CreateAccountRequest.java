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
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Size(max = 100) String lastName,
        @NotBlank @Pattern(regexp = "\\d+", message = "Identity document must contain only digits") @Size(max = 40) String identityDocument,
        @Size(max = 13, message = "Phone must be at most 13 characters") @Pattern(regexp = "^$|^(?:\\+\\d{7,12}|\\d{7,13})$", message = "Phone format is invalid") String phone,
        @PastOrPresent LocalDate birthDate,
        @NotBlank @Email @Size(max = 254) String email,
        @NotBlank @Size(min = 8, max = 72) String password,
        @Positive Long restaurantId) {

    public NewAccountCommand toCommand() {
        return new NewAccountCommand(name, lastName, identityDocument, phone, birthDate, email, password);
    }
}
