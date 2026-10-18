package com.plazoleta.users.application.service;

import com.plazoleta.users.application.port.input.GetUserUseCase;
import com.plazoleta.users.application.port.output.UserAccountRepositoryPort;
import com.plazoleta.users.domain.exception.AccountException;
import com.plazoleta.users.domain.model.UserAccount;

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
