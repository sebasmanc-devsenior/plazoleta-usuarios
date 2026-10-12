package com.plazoleta.users.application.services;

import com.plazoleta.users.application.dto.NewAccountCommand;
import com.plazoleta.users.application.ports.output.PasswordEncoderPort;
import com.plazoleta.users.application.ports.output.UserAccountRepositoryPort;
import com.plazoleta.users.domain.exceptions.AccountException;
import com.plazoleta.users.domain.models.UserAccount;
import com.plazoleta.users.domain.models.UserRole;
import com.plazoleta.users.domain.rules.AccountRules;

import java.util.Locale;

/**
 * Pasos comunes para registrar cualquier cuenta: validar reglas de dominio, impedir correo o
 * documento repetidos, encriptar la clave con BCrypt y guardar con el rol indicado.
 */
public class AccountRegistrar {
    private final UserAccountRepositoryPort accounts;
    private final PasswordEncoderPort passwords;

    public AccountRegistrar(UserAccountRepositoryPort accounts, PasswordEncoderPort passwords) {
        this.accounts = accounts;
        this.passwords = passwords;
    }

    public UserAccount register(NewAccountCommand command, UserRole role, Long restaurantId) {
        if (command == null) {
            throw AccountException.badRequest("Request body is required");
        }
        AccountRules.validateNewAccount(command.name(), command.lastName(), command.identityDocument(),
                command.phone(), command.birthDate(), command.email(), command.password(), role, restaurantId);
        String email = normalizeEmail(command.email());
        String identityDocument = command.identityDocument().trim();
        if (accounts.existsByEmail(email)) {
            throw AccountException.conflict("Email is already registered");
        }
        if (accounts.existsByIdentityDocument(identityDocument)) {
            throw AccountException.conflict("Identity document is already registered");
        }
        String phone = command.phone() == null ? null : command.phone().trim();
        return accounts.save(new UserAccount(null, command.name().trim(), command.lastName().trim(), identityDocument,
                phone, command.birthDate(), email, passwords.encode(command.password()), role, restaurantId));
    }

    static String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
