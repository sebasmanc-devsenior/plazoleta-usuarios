package com.plazoleta.users.infrastructure.adapters.input.rest.dto;

import com.plazoleta.users.domain.models.UserAccount;
import com.plazoleta.users.domain.models.UserRole;

public record EmployeeAccountResponse(Long id, String name, String lastName, String email,
                                      UserRole role, Long restaurantId) {
    public static EmployeeAccountResponse from(UserAccount account) {
        return new EmployeeAccountResponse(account.id(), account.name(), account.lastName(),
                account.email(), account.role(), account.restaurantId());
    }
}
