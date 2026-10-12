package com.plazoleta.users.application.services;

import com.plazoleta.users.application.dto.NewAccountCommand;
import com.plazoleta.users.application.ports.output.PasswordEncoderPort;
import com.plazoleta.users.application.ports.output.UserAccountRepositoryPort;
import com.plazoleta.users.domain.exceptions.AccountException;
import com.plazoleta.users.domain.models.UserAccount;
import com.plazoleta.users.domain.models.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** HU-8: Crear cuenta cliente. */
@ExtendWith(MockitoExtension.class)
class CreateClientServiceTest {
    @Mock
    private UserAccountRepositoryPort accounts;
    @Mock
    private PasswordEncoderPort passwords;
    private CreateClientService service;

    @BeforeEach
    void setUp() {
        service = new CreateClientService(new AccountRegistrar(accounts, passwords));
    }

    private static NewAccountCommand client(String email) {
        return new NewAccountCommand("Laura", "Diaz", "55667788", "+573201112233", null, email, "password123");
    }

    @Test
    void createsAccountWithRoleClientAndBcryptPassword() {
        when(passwords.encode("password123")).thenReturn("$2a$hash");
        when(accounts.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UserAccount created = service.execute(client("laura@test.com"));

        assertThat(created.role()).isEqualTo(UserRole.CLIENTE);
        assertThat(created.passwordHash()).isEqualTo("$2a$hash").isNotEqualTo("password123");
        assertThat(created.restaurantId()).isNull();
    }

    @Test
    void doesNotRequireBirthDate() {
        when(passwords.encode(any())).thenReturn("$2a$hash");
        when(accounts.save(any())).thenAnswer(inv -> inv.getArgument(0));

        assertThat(service.execute(client("laura@test.com")).birthDate()).isNull();
    }

    @Test
    void rejectsInvalidEmail() {
        assertThatThrownBy(() -> service.execute(client("laura-arroba-test")))
                .isInstanceOf(AccountException.class)
                .hasMessageContaining("Email format is invalid");
        verify(accounts, never()).save(any());
    }

    @Test
    void rejectsMissingPhone() {
        NewAccountCommand noPhone = new NewAccountCommand("Laura", "Diaz", "55667788", null, null,
                "laura@test.com", "password123");

        assertThatThrownBy(() -> service.execute(noPhone)).isInstanceOf(AccountException.class);
    }

    @Test
    void rejectsDuplicateEmail() {
        when(accounts.existsByEmail("laura@test.com")).thenReturn(true);

        assertThatThrownBy(() -> service.execute(client("laura@test.com")))
                .isInstanceOf(AccountException.class)
                .hasMessageContaining("Email is already registered");
    }
}
