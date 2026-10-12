package com.plazoleta.users.application.services;

import com.plazoleta.users.application.ports.input.GetUserUseCase;
import com.plazoleta.users.application.ports.output.UserAccountRepositoryPort;
import com.plazoleta.users.domain.exceptions.AccountException;
import com.plazoleta.users.domain.models.UserAccount;

public class GetUserService implements GetUserUseCase {
    private final UserAccountRepositoryPort accounts;

    public GetUserService(UserAccountRepositoryPort accounts) {
        this.accounts = accounts;
    }

    @Override
    public UserAccount execute(long userId) {
        return accounts.findById(userId).orElseThrow(AccountException::notFound);
    }
}
