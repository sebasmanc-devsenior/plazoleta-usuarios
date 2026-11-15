package com.plazoleta.users.infrastructure.configuration;

import com.plazoleta.users.domain.model.AccountRules;
import com.plazoleta.users.domain.util.MensajesError;

import com.plazoleta.users.application.dto.NewAccountCommand;
import com.plazoleta.users.application.port.input.BootstrapAdminPort;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AdminBootstrap implements ApplicationRunner {
    private final BootstrapAdminPort bootstrapAdminUseCase;
    private final String email;
    private final String password;
    private final String name;
    private final String lastName;
    private final String identityDocument;
    private final String phone;
    private final String birthDate;

    public AdminBootstrap(BootstrapAdminPort bootstrapAdminUseCase,
                          @Value("${app.bootstrap.admin.email}") String email,
                          @Value("${app.bootstrap.admin.password}") String password,
                          @Value("${app.bootstrap.admin.name}") String name,
                          @Value("${app.bootstrap.admin.last-name}") String lastName,
                          @Value("${app.bootstrap.admin.identity-document}") String identityDocument,
                          @Value("${app.bootstrap.admin.phone}") String phone,
                          @Value("${app.bootstrap.admin.birth-date}") String birthDate) {
        this.bootstrapAdminUseCase = bootstrapAdminUseCase;
        this.email = email;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.identityDocument = identityDocument;
        this.phone = phone;
        this.birthDate = birthDate;
    }

    @Override
    public void run(ApplicationArguments args) {
        boolean noEmail = email.isBlank();
        boolean noPassword = password.isBlank();
        if (noEmail && noPassword) return;
        if (noEmail || noPassword) {
            throw new IllegalStateException(MensajesError.CONFIGURA_ADMIN_BOOTSTRAP_EMAIL_Y_ADMIN_BOOTSTRAP);
        }
        if (name.isBlank() || lastName.isBlank() || identityDocument.isBlank() || password.length() < AccountRules.PASSWORD_MIN_LENGTH) {
            throw new IllegalStateException(MensajesError.ADMINISTRADOR_INICIAL_NECESITA_NOMBRE_APELLIDO_DOCUMENTO_Y);
        }
        bootstrapAdminUseCase.execute(new NewAccountCommand(name, lastName, identityDocument, blankToNull(phone),
                birthDate.isBlank() ? null : LocalDate.parse(birthDate), email, password));
    }

    private String blankToNull(String value) { return value.isBlank() ? null : value; }
}
