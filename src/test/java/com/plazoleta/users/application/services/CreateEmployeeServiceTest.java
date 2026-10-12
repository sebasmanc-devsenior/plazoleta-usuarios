package com.plazoleta.users.application.services;

import com.plazoleta.users.application.dto.NewAccountCommand;
import com.plazoleta.users.application.ports.output.PasswordEncoderPort;
import com.plazoleta.users.application.ports.output.RestaurantDirectoryPort;
import com.plazoleta.users.application.ports.output.UserAccountRepositoryPort;
import com.plazoleta.users.domain.exceptions.AccountException;
import com.plazoleta.users.domain.models.UserAccount;
import com.plazoleta.users.domain.models.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/** HU-6: Crear cuenta empleado (solo el dueño del restaurante). */
@ExtendWith(MockitoExtension.class)
class CreateEmployeeServiceTest {
    private static final long OWNER_ID = 3L;
    private static final long RESTAURANT_ID = 7L;

    @Mock
    private UserAccountRepositoryPort accounts;
    @Mock
    private PasswordEncoderPort passwords;
    @Mock
    private RestaurantDirectoryPort restaurants;
    private CreateEmployeeService service;

    @BeforeEach
    void setUp() {
        service = new CreateEmployeeService(new AccountRegistrar(accounts, passwords), restaurants);
    }

    private static NewAccountCommand employee() {
        return new NewAccountCommand("Ana", "Perez", "987654321", "+573111234567", null,
                "ana@restaurant.com", "password123");
    }

    @Test
    void createsEmployeeLinkedToRestaurantWhenRequesterIsTheOwner() {
        when(restaurants.findOwnerId(RESTAURANT_ID)).thenReturn(Optional.of(OWNER_ID));
        when(passwords.encode("password123")).thenReturn("$2a$hash");
        when(accounts.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UserAccount created = service.execute(employee(), RESTAURANT_ID, OWNER_ID);

        assertThat(created.role()).isEqualTo(UserRole.EMPLEADO);
        assertThat(created.restaurantId()).isEqualTo(RESTAURANT_ID);
        assertThat(created.passwordHash()).isEqualTo("$2a$hash");
    }

    @Test
    void rejectsOwnerOfAnotherRestaurant() {
        when(restaurants.findOwnerId(RESTAURANT_ID)).thenReturn(Optional.of(99L));

        assertThatThrownBy(() -> service.execute(employee(), RESTAURANT_ID, OWNER_ID))
                .isInstanceOf(AccountException.class)
                .satisfies(ex -> assertThat(((AccountException) ex).status()).isEqualTo(403));
        verify(accounts, never()).save(any());
    }

    @Test
    void rejectsRestaurantThatDoesNotExist() {
        when(restaurants.findOwnerId(RESTAURANT_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.execute(employee(), RESTAURANT_ID, OWNER_ID))
                .isInstanceOf(AccountException.class)
                .satisfies(ex -> assertThat(((AccountException) ex).status()).isEqualTo(404));
        verify(accounts, never()).save(any());
    }

    @Test
    void requiresPositiveRestaurantIdBeforeCallingPlazoleta() {
        assertThatThrownBy(() -> service.execute(employee(), null, OWNER_ID))
                .isInstanceOf(AccountException.class)
                .satisfies(ex -> assertThat(((AccountException) ex).status()).isEqualTo(400));
        assertThatThrownBy(() -> service.execute(employee(), 0L, OWNER_ID)).isInstanceOf(AccountException.class);
        verifyNoInteractions(restaurants);
    }

    @Test
    void stillValidatesEmployeeData() {
        when(restaurants.findOwnerId(RESTAURANT_ID)).thenReturn(Optional.of(OWNER_ID));
        NewAccountCommand badDocument = new NewAccountCommand("Ana", "Perez", "ABC", "+573111234567", null,
                "ana@restaurant.com", "password123");

        assertThatThrownBy(() -> service.execute(badDocument, RESTAURANT_ID, OWNER_ID))
                .isInstanceOf(AccountException.class)
                .hasMessageContaining("only digits");
    }
}
