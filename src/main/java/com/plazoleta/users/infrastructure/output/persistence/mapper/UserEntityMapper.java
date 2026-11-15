package com.plazoleta.users.infrastructure.output.persistence.mapper;

import com.plazoleta.users.domain.model.UserAccount;
import com.plazoleta.users.infrastructure.output.persistence.entity.UserEntity;

/** Convierte entre el modelo del dominio UserAccount y la entidad JPA de la tabla users. */
public final class UserEntityMapper {
    private UserEntityMapper() {
    }

    public static UserEntity toEntity(UserAccount user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.id());
        entity.setName(user.name());
        entity.setLastName(user.lastName());
        entity.setIdentityDocument(user.identityDocument());
        entity.setPhone(user.phone());
        entity.setBirthDate(user.birthDate());
        entity.setEmail(user.email());
        entity.setPasswordHash(user.passwordHash());
        entity.setRole(user.role());
        entity.setRestaurantId(user.restaurantId());
        return entity;
    }

    public static UserAccount toDomain(UserEntity entity) {
        return new UserAccount(entity.getId(), entity.getName(), entity.getLastName(), entity.getIdentityDocument(),
                entity.getPhone(), entity.getBirthDate(), entity.getEmail(), entity.getPasswordHash(), entity.getRole(),
                entity.getRestaurantId());
    }
}
