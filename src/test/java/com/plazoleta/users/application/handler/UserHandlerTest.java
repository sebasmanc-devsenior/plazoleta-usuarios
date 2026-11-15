package com.plazoleta.users.application.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.plazoleta.users.application.dto.LoginResult;
import com.plazoleta.users.application.dto.NewAccountCommand;
import com.plazoleta.users.application.port.input.CreateClientPort;
import com.plazoleta.users.application.port.input.CreateEmployeePort;
import com.plazoleta.users.application.port.input.CreateOwnerPort;
import com.plazoleta.users.application.port.input.GetUserPort;
import com.plazoleta.users.application.port.input.LoginPort;
import com.plazoleta.users.domain.model.UserAccount;

/** El handler no decide nada: pasa los datos al caso de uso que corresponde y devuelve su respuesta. */
class UserHandlerTest {
    private final CreateOwnerPort createOwnerUseCase = mock(CreateOwnerPort.class);
    private final LoginPort loginUseCase = mock(LoginPort.class);
    private final CreateEmployeePort createEmployeeUseCase = mock(CreateEmployeePort.class);
    private final CreateClientPort createClientUseCase = mock(CreateClientPort.class);
    private final GetUserPort getUserUseCase = mock(GetUserPort.class);
    private final UserHandler handler = new UserHandler(createOwnerUseCase, loginUseCase, createEmployeeUseCase,
            createClientUseCase, getUserUseCase);

    private final NewAccountCommand command = new NewAccountCommand("Ana", "Ruiz", "123", "+573001112233",
            LocalDate.of(1990, 1, 1), "ana@plazoleta.test", "secreta");
    private final UserAccount account = mock(UserAccount.class);

    @Test
    void createOwnerDelegatesToItsUseCase() {
        when(createOwnerUseCase.execute(command)).thenReturn(account);

        assertThat(handler.createOwner(command)).isSameAs(account);
        verify(createOwnerUseCase).execute(command);
    }

    @Test
    void loginDelegatesToItsUseCase() {
        LoginResult result = new LoginResult("jwt", 1L, "Ana", "Ruiz", "ana@plazoleta.test", "CLIENTE");
        when(loginUseCase.execute("ana@plazoleta.test", "secreta")).thenReturn(result);

        assertThat(handler.login("ana@plazoleta.test", "secreta")).isSameAs(result);
    }

    @Test
    void createEmployeePassesTheRestaurantAndTheOwnerFromTheToken() {
        when(createEmployeeUseCase.execute(command, 4L, 9L)).thenReturn(account);

        assertThat(handler.createEmployee(command, 4L, 9L)).isSameAs(account);
        verify(createEmployeeUseCase).execute(command, 4L, 9L);
    }

    @Test
    void createClientDelegatesToItsUseCase() {
        when(createClientUseCase.execute(command)).thenReturn(account);

        assertThat(handler.createClient(command)).isSameAs(account);
        verify(createClientUseCase).execute(command);
    }

    @Test
    void getUserDelegatesToItsUseCase() {
        when(getUserUseCase.execute(7L)).thenReturn(account);

        assertThat(handler.getUser(7L)).isSameAs(account);
        verify(getUserUseCase).execute(7L);
    }
}
