package com.plazoleta.users.infrastructure.configuration;

import com.plazoleta.users.application.port.input.BootstrapAdminUseCase;
import com.plazoleta.users.application.port.input.CreateClientUseCase;
import com.plazoleta.users.application.port.input.CreateEmployeeUseCase;
import com.plazoleta.users.application.port.input.CreateOwnerUseCase;
import com.plazoleta.users.application.port.input.GetUserUseCase;
import com.plazoleta.users.application.port.input.LoginUseCase;
import com.plazoleta.users.application.port.output.PasswordEncoderPort;
import com.plazoleta.users.application.port.output.RestaurantDirectoryPort;
import com.plazoleta.users.application.port.output.TokenProviderPort;
import com.plazoleta.users.application.port.output.UserAccountRepositoryPort;
import com.plazoleta.users.application.service.AccountRegistrar;
import com.plazoleta.users.application.service.BootstrapAdminService;
import com.plazoleta.users.application.service.CreateClientService;
import com.plazoleta.users.application.service.CreateEmployeeService;
import com.plazoleta.users.application.service.CreateOwnerService;
import com.plazoleta.users.application.service.GetUserService;
import com.plazoleta.users.application.service.LoginService;
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
    public CreateOwnerUseCase createOwnerUseCase(AccountRegistrar registrar) {
        return new CreateOwnerService(registrar);
    }

    /** HU-2: consulta interna que usa el microservicio de restaurantes para validar al propietario. */
    @Bean
    public GetUserUseCase getUserUseCase(UserAccountRepositoryPort accounts) {
        return new GetUserService(accounts);
    }

    /** HU-5 */
    @Bean
    public LoginUseCase loginUseCase(UserAccountRepositoryPort accounts, PasswordEncoderPort passwords,
                                     TokenProviderPort tokens) {
        return new LoginService(accounts, passwords, tokens);
    }

    /** HU-5: administrador inicial, necesario para crear propietarios. */
    @Bean
    public BootstrapAdminUseCase bootstrapAdminUseCase(UserAccountRepositoryPort accounts, AccountRegistrar registrar) {
        return new BootstrapAdminService(accounts, registrar);
    }

    /** HU-6 */
    @Bean
    public CreateEmployeeUseCase createEmployeeUseCase(AccountRegistrar registrar, RestaurantDirectoryPort restaurants) {
        return new CreateEmployeeService(registrar, restaurants);
    }

    /** HU-8 */
    @Bean
    public CreateClientUseCase createClientUseCase(AccountRegistrar registrar) {
        return new CreateClientService(registrar);
    }
}
