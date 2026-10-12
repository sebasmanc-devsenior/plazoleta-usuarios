package com.plazoleta.users.application.dto;

import java.time.LocalDate;

/**
 * Datos personales para registrar una cuenta. El rol no viene aquí: lo decide cada caso de uso
 * (HU-1 propietario, HU-6 empleado, HU-8 cliente).
 */
public record NewAccountCommand(
        String name,
        String lastName,
        String identityDocument,
        String phone,
        LocalDate birthDate,
        String email,
        String password) {
}
