package com.plazoleta.users.application.services;

import com.plazoleta.users.application.dto.LoginResult;
import com.plazoleta.users.application.ports.input.LoginUseCase;
import com.plazoleta.users.application.ports.output.PasswordEncoderPort;
import com.plazoleta.users.application.ports.output.TokenProviderPort;
import com.plazoleta.users.application.ports.output.UserAccountRepositoryPort;
import com.plazoleta.users.domain.exceptions.AccountException;
import com.plazoleta.users.domain.models.UserAccount;

/**
 * HU-5: valida correo y clave y emite un JWT con el id, el rol y (para empleados) el restaurante.
 * Los intentos son ilimitados y el error no revela si falló el correo o la clave.
 */
public class LoginService implements LoginUseCase {
    private final UserAccountRepositoryPort accounts;
    private final PasswordEncoderPort passwords;
    private final TokenProviderPort tokens;

    public LoginService(UserAccountRepositoryPort accounts, PasswordEncoderPort passwords, TokenProviderPort tokens) {
        this.accounts = accounts;
        this.passwords = passwords;
        this.tokens = tokens;
    }

    @Override
    public LoginResult execute(String email, String password) {
        if (email == null || password == null) {
            throw AccountException.unauthorized();
        }
        UserAccount user = accounts.findByEmail(AccountRegistrar.normalizeEmail(email))
                .filter(account -> passwords.matches(password, account.passwordHash()))
                .orElseThrow(AccountException::unauthorized);
        return new LoginResult(tokens.issue(user), user.id(), user.role().name());
    }
}
