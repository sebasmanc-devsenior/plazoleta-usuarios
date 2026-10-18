package com.plazoleta.users.application.port.output;

import com.plazoleta.users.domain.model.UserAccount;

public interface TokenProviderPort {
    String issue(UserAccount user);
}
