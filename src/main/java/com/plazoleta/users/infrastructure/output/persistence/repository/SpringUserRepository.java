package com.plazoleta.users.infrastructure.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import com.plazoleta.users.infrastructure.output.persistence.entity.UserEntity;

public interface SpringUserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByIdentityDocument(String identityDocument);
    Optional<UserEntity> findByEmailIgnoreCase(String email);
}
