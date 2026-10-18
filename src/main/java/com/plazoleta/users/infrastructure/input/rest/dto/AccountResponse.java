package com.plazoleta.users.infrastructure.input.rest.dto;

import com.plazoleta.users.domain.model.UserAccount;
import com.plazoleta.users.domain.model.UserRole;

public record AccountResponse(Long id, String name, String lastName, String email, UserRole role) {
    public static AccountResponse from(UserAccount account) {
        return new AccountResponse(account.id(), account.name(), account.lastName(), account.email(), account.role());
    }
}
