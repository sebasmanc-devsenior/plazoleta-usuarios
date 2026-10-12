package com.plazoleta.users.infrastructure.adapters.output.persistence;

import com.plazoleta.users.domain.models.UserAccount;
import com.plazoleta.users.domain.models.UserRole;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
        @UniqueConstraint(name = "uk_users_identity_document", columnNames = "identity_document")
})
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;
    @Column(name = "identity_document", nullable = false, length = 40)
    private String identityDocument;
    @Column(length = 20)
    private String phone;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(nullable = false, length = 254)
    private String email;
    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;
    @Column(name = "restaurant_id")
    private Long restaurantId;

    protected UserEntity() {}

    public UserEntity(UserAccount user) {
        this.id = user.id();
        this.name = user.name();
        this.lastName = user.lastName();
        this.identityDocument = user.identityDocument();
        this.phone = user.phone();
        this.birthDate = user.birthDate();
        this.email = user.email();
        this.passwordHash = user.passwordHash();
        this.role = user.role();
        this.restaurantId = user.restaurantId();
    }

    public UserAccount toDomain() {
        return new UserAccount(id, name, lastName, identityDocument, phone, birthDate, email, passwordHash, role, restaurantId);
    }
}
