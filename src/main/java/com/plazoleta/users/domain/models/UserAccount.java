package com.plazoleta.users.domain.models;

import java.time.LocalDate;

public record UserAccount(
        Long id,
        String name,
        String lastName,
        String identityDocument,
        String phone,
        LocalDate birthDate,
        String email,
        String passwordHash,
        UserRole role,
        Long restaurantId) {
}
