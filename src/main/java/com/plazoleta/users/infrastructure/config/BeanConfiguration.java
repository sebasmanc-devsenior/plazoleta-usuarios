package com.plazoleta.users.infrastructure.config;

import com.plazoleta.users.application.ports.input.CreateOwnerUseCase;
import com.plazoleta.users.application.ports.input.GetUserUseCase;
import com.plazoleta.users.application.ports.output.PasswordEncoderPort;
import com.plazoleta.users.application.ports.output.UserAccountRepositoryPort;
import com.plazoleta.users.application.services.AccountRegistrar;
import com.plazoleta.users.application.services.CreateOwnerService;
import com.plazoleta.users.application.services.GetUserService;
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
}
