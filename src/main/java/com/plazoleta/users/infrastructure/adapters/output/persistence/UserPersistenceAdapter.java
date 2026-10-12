package com.plazoleta.users.infrastructure.adapters.output.persistence;

import com.plazoleta.users.application.ports.output.UserAccountRepositoryPort;
import com.plazoleta.users.domain.models.UserAccount;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserPersistenceAdapter implements UserAccountRepositoryPort {
    private final SpringUserRepository repository;

    public UserPersistenceAdapter(SpringUserRepository repository) {
        this.repository = repository;
    }

    @Override public boolean existsByEmail(String email) { return repository.existsByEmailIgnoreCase(email); }
    @Override public boolean existsByIdentityDocument(String identityDocument) {
        return repository.existsByIdentityDocument(identityDocument);
    }
    @Override public Optional<UserAccount> findByEmail(String email) {
        return repository.findByEmailIgnoreCase(email).map(UserEntity::toDomain);
    }
    @Override public Optional<UserAccount> findById(long id) {
        return repository.findById(id).map(UserEntity::toDomain);
    }
    @Override public UserAccount save(UserAccount user) {
        return repository.save(new UserEntity(user)).toDomain();
    }
}
