package com.plazoleta.users.application.port.output;

import com.plazoleta.users.domain.model.UserAccount;

import java.util.Optional;

public interface UserAccountRepositoryPort {
    boolean existsByEmail(String email);
    boolean existsByIdentityDocument(String identityDocument);
    Optional<UserAccount> findByEmail(String email);
    Optional<UserAccount> findById(long id);
    UserAccount save(UserAccount user);
}
