package com.plazoleta.users.infrastructure.adapters.input.rest.dto;

import com.plazoleta.users.domain.models.UserAccount;
import com.plazoleta.users.domain.models.UserRole;

public record InternalUserResponse(
        Long id, UserRole role, String identityDocument, String phone, Long restaurantId) {
    public static InternalUserResponse from(UserAccount user) {
        return new InternalUserResponse(user.id(), user.role(), user.identityDocument(), user.phone(), user.restaurantId());
    }
}
