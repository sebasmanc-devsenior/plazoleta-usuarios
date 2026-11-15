package com.plazoleta.users.application.usecase;

import com.plazoleta.users.application.port.input.GetUserPort;
import com.plazoleta.users.application.port.output.UserAccountRepositoryPort;
import com.plazoleta.users.domain.exception.AccountException;
import com.plazoleta.users.domain.model.UserAccount;

public class GetUserUseCase implements GetUserPort {
    private final UserAccountRepositoryPort accounts;

    public GetUserUseCase(UserAccountRepositoryPort accounts) {
        this.accounts = accounts;
    }

    @Override
    public UserAccount execute(long userId) {
        return accounts.findById(userId).orElseThrow(AccountException::notFound);
    }
}
