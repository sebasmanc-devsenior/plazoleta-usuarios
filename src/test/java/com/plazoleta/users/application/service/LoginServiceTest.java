package com.plazoleta.users.application.service;

import com.plazoleta.users.application.dto.LoginResult;
import com.plazoleta.users.application.port.output.PasswordEncoderPort;
import com.plazoleta.users.application.port.output.TokenProviderPort;
import com.plazoleta.users.application.port.output.UserAccountRepositoryPort;
import com.plazoleta.users.domain.exception.AccountException;
import com.plazoleta.users.domain.model.UserAccount;
import com.plazoleta.users.domain.model.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** HU-5: Autenticación con correo y clave. */
@ExtendWith(MockitoExtension.class)
class LoginServiceTest {
    @Mock
    private UserAccountRepositoryPort accounts;
    @Mock
    private PasswordEncoderPort passwords;
    @Mock
    private TokenProviderPort tokens;
    @InjectMocks
    private LoginService service;

    private static UserAccount carlos() {
        return new UserAccount(10L, "Carlos", "Gomez", "123456", "+573001234567",
                LocalDate.of(1990, 1, 1), "carlos@test.com", "$2a$hashed", UserRole.PROPIETARIO, null);
    }

    @Test
    void returnsTokenUserIdAndRoleWhenCredentialsAreValid() {
        when(accounts.findByEmail("carlos@test.com")).thenReturn(Optional.of(carlos()));
        when(passwords.matches("plainPassword", "$2a$hashed")).thenReturn(true);
        when(tokens.issue(any())).thenReturn("jwt.token.mock");

        LoginResult result = service.execute("carlos@test.com", "plainPassword");

        assertThat(result.token()).isEqualTo("jwt.token.mock");
        assertThat(result.userId()).isEqualTo(10L);
        assertThat(result.role()).isEqualTo("PROPIETARIO");
    }

    @Test
    void emailIsCaseInsensitive() {
        when(accounts.findByEmail("carlos@test.com")).thenReturn(Optional.of(carlos()));
        when(passwords.matches("plainPassword", "$2a$hashed")).thenReturn(true);
        when(tokens.issue(any())).thenReturn("jwt");

        assertThat(service.execute(" CARLOS@test.com ", "plainPassword").userId()).isEqualTo(10L);
    }

    @Test
    void rejectsWrongPasswordWithoutIssuingToken() {
        when(accounts.findByEmail("carlos@test.com")).thenReturn(Optional.of(carlos()));
        when(passwords.matches("wrongPassword", "$2a$hashed")).thenReturn(false);

        assertThatThrownBy(() -> service.execute("carlos@test.com", "wrongPassword"))
                .isInstanceOf(AccountException.class)
                .hasMessageContaining("Invalid email or password");
        verify(tokens, never()).issue(any());
    }

    @Test
    void rejectsUnknownEmailWithTheSameMessage() {
        when(accounts.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.execute("unknown@test.com", "password"))
                .isInstanceOf(AccountException.class)
                .hasMessageContaining("Invalid email or password");
    }

    @Test
    void allowsUnlimitedAttempts() {
        when(accounts.findByEmail("carlos@test.com")).thenReturn(Optional.of(carlos()));
        when(passwords.matches("wrong", "$2a$hashed")).thenReturn(false);
        when(passwords.matches("plainPassword", "$2a$hashed")).thenReturn(true);
        when(tokens.issue(any())).thenReturn("jwt");

        for (int i = 0; i < 10; i++) {
            assertThatThrownBy(() -> service.execute("carlos@test.com", "wrong")).isInstanceOf(AccountException.class);
        }
        assertThat(service.execute("carlos@test.com", "plainPassword").token()).isEqualTo("jwt");
    }

    @Test
    void rejectsMissingCredentials() {
        assertThatThrownBy(() -> service.execute(null, "x")).isInstanceOf(AccountException.class);
        assertThatThrownBy(() -> service.execute("carlos@test.com", null)).isInstanceOf(AccountException.class);
    }
}
