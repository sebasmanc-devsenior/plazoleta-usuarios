package com.plazoleta.users.infrastructure.output.persistence.adapter;

import com.plazoleta.users.application.port.output.UserAccountRepositoryPort;
import com.plazoleta.users.domain.model.UserAccount;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import com.plazoleta.users.infrastructure.output.persistence.repository.SpringUserRepository;
import com.plazoleta.users.infrastructure.output.persistence.entity.UserEntity;

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
