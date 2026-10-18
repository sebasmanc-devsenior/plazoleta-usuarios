package com.plazoleta.users.infrastructure.input.rest.dto;

import com.plazoleta.users.domain.model.UserAccount;
import com.plazoleta.users.domain.model.UserRole;

public record InternalUserResponse(
        Long id, UserRole role, String identityDocument, String phone, Long restaurantId) {
    public static InternalUserResponse from(UserAccount user) {
        return new InternalUserResponse(user.id(), user.role(), user.identityDocument(), user.phone(), user.restaurantId());
    }
}
