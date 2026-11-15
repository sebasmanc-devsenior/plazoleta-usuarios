package com.plazoleta.users.infrastructure.configuration;

import com.plazoleta.users.application.handler.IUserHandler;
import com.plazoleta.users.application.handler.UserHandler;
import com.plazoleta.users.application.port.input.BootstrapAdminPort;
import com.plazoleta.users.application.port.input.CreateClientPort;
import com.plazoleta.users.application.port.input.CreateEmployeePort;
import com.plazoleta.users.application.port.input.CreateOwnerPort;
import com.plazoleta.users.application.port.input.GetUserPort;
import com.plazoleta.users.application.port.input.LoginPort;
import com.plazoleta.users.application.port.output.PasswordEncoderPort;
import com.plazoleta.users.application.port.output.RestaurantDirectoryPort;
import com.plazoleta.users.application.port.output.TokenProviderPort;
import com.plazoleta.users.application.port.output.UserAccountRepositoryPort;
import com.plazoleta.users.application.usecase.AccountRegistrar;
import com.plazoleta.users.application.usecase.BootstrapAdminUseCase;
import com.plazoleta.users.application.usecase.CreateClientUseCase;
import com.plazoleta.users.application.usecase.CreateEmployeeUseCase;
import com.plazoleta.users.application.usecase.CreateOwnerUseCase;
import com.plazoleta.users.application.usecase.GetUserUseCase;
import com.plazoleta.users.application.usecase.LoginUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Inyección de dependencias: conecta los casos de uso (application, sin Spring) con los
 * adaptadores de salida (infrastructure). Es el único lugar que conoce ambos lados.
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public AccountRegistrar accountRegistrar(UserAccountRepositoryPort accounts, PasswordEncoderPort passwords) {
        return new AccountRegistrar(accounts, passwords);
    }

    /** HU-1 */
    @Bean
    public CreateOwnerPort createOwnerUseCase(AccountRegistrar registrar) {
        return new CreateOwnerUseCase(registrar);
    }

    /** HU-2: consulta interna que usa el microservicio de restaurantes para validar al propietario. */
    @Bean
    public GetUserPort getUserUseCase(UserAccountRepositoryPort accounts) {
        return new GetUserUseCase(accounts);
    }

    /** HU-5 */
    @Bean
    public LoginPort loginUseCase(UserAccountRepositoryPort accounts, PasswordEncoderPort passwords,
                                     TokenProviderPort tokens) {
        return new LoginUseCase(accounts, passwords, tokens);
    }

    /** HU-5: administrador inicial, necesario para crear propietarios. */
    @Bean
    public BootstrapAdminPort bootstrapAdminUseCase(UserAccountRepositoryPort accounts, AccountRegistrar registrar) {
        return new BootstrapAdminUseCase(accounts, registrar);
    }

    /** HU-6 */
    @Bean
    public CreateEmployeePort createEmployeeUseCase(AccountRegistrar registrar, RestaurantDirectoryPort restaurants) {
        return new CreateEmployeeUseCase(registrar, restaurants);
    }

    /** HU-8 */
    @Bean
    public CreateClientPort createClientUseCase(AccountRegistrar registrar) {
        return new CreateClientUseCase(registrar);
    }

    /** Handler que usan los controladores de usuarios: delega en los casos de uso. */
    @Bean
    public IUserHandler userHandler(CreateOwnerPort createOwnerUseCase, LoginPort loginUseCase,
                                    CreateEmployeePort createEmployeeUseCase,
                                    CreateClientPort createClientUseCase, GetUserPort getUserUseCase) {
        return new UserHandler(createOwnerUseCase, loginUseCase, createEmployeeUseCase, createClientUseCase,
                getUserUseCase);
    }
}
