package com.plazoleta.users.infrastructure.output.persistence.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.plazoleta.users.domain.model.UserAccount;
import com.plazoleta.users.domain.model.UserRole;
import com.plazoleta.users.infrastructure.output.persistence.entity.UserEntity;

/** El mapper pasa la cuenta del dominio a la entidad JPA y de vuelta sin perder ningún dato. */
class UserEntityMapperTest {

    @Test
    void anEmployeeGoesToTheEntityAndBackWithAllItsFields() {
        UserAccount employee = new UserAccount(4L, "Luis", "Gómez", "1030405060", "+573005556677",
                LocalDate.of(1995, 3, 2), "luis@correo.com", "$2a$10$hash", UserRole.EMPLEADO, 3L);

        UserEntity entity = UserEntityMapper.toEntity(employee);

        assertThat(entity.getPasswordHash()).isEqualTo("$2a$10$hash");
        assertThat(entity.getRestaurantId()).isEqualTo(3L);
        assertThat(UserEntityMapper.toDomain(entity)).isEqualTo(employee);
    }

    @Test
    void aClientWithoutBirthDateNorRestaurantKeepsThoseFieldsEmpty() {
        UserAccount client = new UserAccount(null, "María", "Ruiz", "1040506070", "+573118889900", null,
                "maria@correo.com", "$2a$10$hash", UserRole.CLIENTE, null);

        UserAccount back = UserEntityMapper.toDomain(UserEntityMapper.toEntity(client));

        assertThat(back).isEqualTo(client);
        assertThat(back.birthDate()).isNull();
        assertThat(back.restaurantId()).isNull();
    }
}
