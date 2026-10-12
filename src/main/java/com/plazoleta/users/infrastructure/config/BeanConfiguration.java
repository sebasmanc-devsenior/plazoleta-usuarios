package com.plazoleta.users.infrastructure.config;

import com.plazoleta.users.application.ports.input.BootstrapAdminUseCase;
import com.plazoleta.users.application.ports.input.CreateOwnerUseCase;
import com.plazoleta.users.application.ports.input.GetUserUseCase;
import com.plazoleta.users.application.ports.input.LoginUseCase;
import com.plazoleta.users.application.ports.output.PasswordEncoderPort;
import com.plazoleta.users.application.ports.output.TokenProviderPort;
import com.plazoleta.users.application.ports.output.UserAccountRepositoryPort;
import com.plazoleta.users.application.services.AccountRegistrar;
import com.plazoleta.users.application.services.BootstrapAdminService;
import com.plazoleta.users.application.services.CreateOwnerService;
import com.plazoleta.users.application.services.GetUserService;
import com.plazoleta.users.application.services.LoginService;
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
}
