package com.plazoleta.users.application.handler;

import com.plazoleta.users.application.dto.LoginResult;
import com.plazoleta.users.application.dto.NewAccountCommand;
import com.plazoleta.users.application.port.input.CreateClientPort;
import com.plazoleta.users.application.port.input.CreateEmployeePort;
import com.plazoleta.users.application.port.input.CreateOwnerPort;
import com.plazoleta.users.application.port.input.GetUserPort;
import com.plazoleta.users.application.port.input.LoginPort;
import com.plazoleta.users.domain.model.UserAccount;

/** Delega en los casos de uso de usuarios. Se registra como @Bean en BeanConfiguration (sin Spring aquí). */
public class UserHandler implements IUserHandler {
    private final CreateOwnerPort createOwnerUseCase;
    private final LoginPort loginUseCase;
    private final CreateEmployeePort createEmployeeUseCase;
    private final CreateClientPort createClientUseCase;
    private final GetUserPort getUserUseCase;

    public UserHandler(CreateOwnerPort createOwnerUseCase, LoginPort loginUseCase,
                       CreateEmployeePort createEmployeeUseCase, CreateClientPort createClientUseCase,
                       GetUserPort getUserUseCase) {
        this.createOwnerUseCase = createOwnerUseCase;
        this.loginUseCase = loginUseCase;
        this.createEmployeeUseCase = createEmployeeUseCase;
        this.createClientUseCase = createClientUseCase;
        this.getUserUseCase = getUserUseCase;
    }

    @Override
    public UserAccount createOwner(NewAccountCommand command) {
        return createOwnerUseCase.execute(command);
    }

    @Override
    public LoginResult login(String email, String password) {
        return loginUseCase.execute(email, password);
    }

    @Override
    public UserAccount createEmployee(NewAccountCommand command, Long restaurantId, long requesterId) {
        return createEmployeeUseCase.execute(command, restaurantId, requesterId);
    }

    @Override
    public UserAccount createClient(NewAccountCommand command) {
        return createClientUseCase.execute(command);
    }

    @Override
    public UserAccount getUser(long userId) {
        return getUserUseCase.execute(userId);
    }
}
