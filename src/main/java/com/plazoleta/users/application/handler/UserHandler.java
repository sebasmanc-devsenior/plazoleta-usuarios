package com.plazoleta.users.application.handler;

import com.plazoleta.users.application.dto.LoginResult;
import com.plazoleta.users.application.dto.NewAccountCommand;
import com.plazoleta.users.application.port.input.CreateClientUseCase;
import com.plazoleta.users.application.port.input.CreateEmployeeUseCase;
import com.plazoleta.users.application.port.input.CreateOwnerUseCase;
import com.plazoleta.users.application.port.input.GetUserUseCase;
import com.plazoleta.users.application.port.input.LoginUseCase;
import com.plazoleta.users.domain.model.UserAccount;

/** Delega en los casos de uso de usuarios. Se registra como @Bean en BeanConfiguration (sin Spring aquí). */
public class UserHandler implements IUserHandler {
    private final CreateOwnerUseCase createOwnerUseCase;
    private final LoginUseCase loginUseCase;
    private final CreateEmployeeUseCase createEmployeeUseCase;
    private final CreateClientUseCase createClientUseCase;
    private final GetUserUseCase getUserUseCase;

    public UserHandler(CreateOwnerUseCase createOwnerUseCase, LoginUseCase loginUseCase,
                       CreateEmployeeUseCase createEmployeeUseCase, CreateClientUseCase createClientUseCase,
                       GetUserUseCase getUserUseCase) {
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
