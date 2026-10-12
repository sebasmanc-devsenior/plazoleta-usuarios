package com.plazoleta.users.application.ports.output;

import com.plazoleta.users.domain.models.UserAccount;

public interface TokenProviderPort {
    String issue(UserAccount user);
}
