package com.plazoleta.users.infrastructure.adapters.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface SpringUserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByIdentityDocument(String identityDocument);
    Optional<UserEntity> findByEmailIgnoreCase(String email);
}
