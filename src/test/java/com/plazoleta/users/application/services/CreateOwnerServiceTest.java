package com.plazoleta.users.application.services;

import com.plazoleta.users.application.dto.NewAccountCommand;
import com.plazoleta.users.application.ports.output.PasswordEncoderPort;
import com.plazoleta.users.application.ports.output.UserAccountRepositoryPort;
import com.plazoleta.users.domain.exceptions.AccountException;
import com.plazoleta.users.domain.models.UserAccount;
import com.plazoleta.users.domain.models.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** HU-1: Crear propietario. */
@ExtendWith(MockitoExtension.class)
class CreateOwnerServiceTest {
    @Mock
    private UserAccountRepositoryPort accounts;
    @Mock
    private PasswordEncoderPort passwords;
    private CreateOwnerService service;

    @BeforeEach
    void setUp() {
        service = new CreateOwnerService(new AccountRegistrar(accounts, passwords));
    }

    private static NewAccountCommand owner(String document, String phone, LocalDate birthDate, String email) {
        return new NewAccountCommand("Carlos", "Gomez", document, phone, birthDate, email, "securePassword123");
    }

    private static NewAccountCommand validOwner() {
        return owner("100200300", "+573005698325", LocalDate.now().minusYears(25), "owner@plazoleta.com");
    }

    private void saveReturnsWithId() {
        when(accounts.save(any())).thenAnswer(inv -> {
            UserAccount ua = inv.getArgument(0);
            return new UserAccount(1L, ua.name(), ua.lastName(), ua.identityDocument(), ua.phone(),
                    ua.birthDate(), ua.email(), ua.passwordHash(), ua.role(), ua.restaurantId());
        });
    }

    @Test
    void createsOwnerWithRoleOwnerAndBcryptPassword() {
        when(passwords.encode("securePassword123")).thenReturn("$2a$hashedPassword");
        saveReturnsWithId();

        UserAccount created = service.execute(validOwner());

        ArgumentCaptor<UserAccount> saved = ArgumentCaptor.forClass(UserAccount.class);
        verify(accounts).save(saved.capture());
        assertThat(saved.getValue().role()).isEqualTo(UserRole.PROPIETARIO);
        assertThat(saved.getValue().passwordHash()).isEqualTo("$2a$hashedPassword");
        assertThat(saved.getValue().restaurantId()).isNull();
        assertThat(created.id()).isEqualTo(1L);
    }

    @Test
    void normalizesEmailBeforeSaving() {
        when(passwords.encode(any())).thenReturn("$2a$hash");
        saveReturnsWithId();

        UserAccount created = service.execute(owner("100200300", "+573005698325",
                LocalDate.of(1990, 1, 1), " Owner@Plazoleta.COM "));

        assertThat(created.email()).isEqualTo("owner@plazoleta.com");
    }

    @Test
    void rejectsUnderageOwner() {
        assertThatThrownBy(() -> service.execute(owner("100200300", "+573005698325",
                LocalDate.now().minusYears(17), "owner@plazoleta.com")))
                .isInstanceOf(AccountException.class)
                .hasMessageContaining("18 years");
        verify(accounts, never()).save(any());
    }

    @Test
    void rejectsOwnerWithoutBirthDate() {
        assertThatThrownBy(() -> service.execute(owner("100200300", "+573005698325", null, "owner@plazoleta.com")))
                .isInstanceOf(AccountException.class);
    }

    @Test
    void acceptsOwnerWhoTurnedEighteenToday() {
        when(passwords.encode(any())).thenReturn("$2a$hash");
        saveReturnsWithId();

        assertThat(service.execute(owner("100200300", "+573005698325",
                LocalDate.now().minusYears(18), "owner@plazoleta.com")).role()).isEqualTo(UserRole.PROPIETARIO);
    }

    @ParameterizedTest
    @ValueSource(strings = {"correo-invalido", "sin-arroba.com", "a@b", "@plazoleta.com"})
    void rejectsInvalidEmail(String email) {
        assertThatThrownBy(() -> service.execute(owner("100200300", "+573005698325",
                LocalDate.of(1990, 1, 1), email)))
                .isInstanceOf(AccountException.class)
                .hasMessageContaining("Email format is invalid");
    }

    @ParameterizedTest
    @ValueSource(strings = {"ABC-123", "1002 00300", "10.200.300", ""})
    void rejectsNonNumericIdentityDocument(String document) {
        assertThatThrownBy(() -> service.execute(owner(document, "+573005698325",
                LocalDate.of(1990, 1, 1), "owner@plazoleta.com")))
                .isInstanceOf(AccountException.class)
                .hasMessageContaining("only digits");
    }

    @ParameterizedTest
    @ValueSource(strings = {"+5730056983259", "30056983A5", "300-569-8325", "57+3005698325"})
    void rejectsPhoneLongerThan13CharactersOrWithInvalidSymbols(String phone) {
        assertThatThrownBy(() -> service.execute(owner("100200300", phone,
                LocalDate.of(1990, 1, 1), "owner@plazoleta.com")))
                .isInstanceOf(AccountException.class)
                .hasMessageContaining("Phone must be at most 13 characters");
    }

    @ParameterizedTest
    @ValueSource(strings = {"+573005698325", "3005698325"})
    void acceptsPhoneWithOrWithoutPlusSign(String phone) {
        when(passwords.encode(any())).thenReturn("$2a$hash");
        saveReturnsWithId();

        assertThat(service.execute(owner("100200300", phone, LocalDate.of(1990, 1, 1),
                "owner@plazoleta.com")).phone()).isEqualTo(phone);
    }

    @Test
    void rejectsDuplicateEmail() {
        when(accounts.existsByEmail("owner@plazoleta.com")).thenReturn(true);

        assertThatThrownBy(() -> service.execute(validOwner()))
                .isInstanceOf(AccountException.class)
                .hasMessageContaining("Email is already registered");
        verify(accounts, never()).save(any());
    }

    @Test
    void rejectsDuplicateIdentityDocument() {
        when(accounts.existsByIdentityDocument("100200300")).thenReturn(true);

        assertThatThrownBy(() -> service.execute(validOwner()))
                .isInstanceOf(AccountException.class)
                .hasMessageContaining("Identity document is already registered");
        verify(accounts, never()).save(any());
    }

    @Test
    void rejectsMissingRequiredFields() {
        NewAccountCommand noName = new NewAccountCommand(" ", "Gomez", "100200300", "+573005698325",
                LocalDate.of(1990, 1, 1), "owner@plazoleta.com", "securePassword123");
        NewAccountCommand noPassword = new NewAccountCommand("Carlos", "Gomez", "100200300", "+573005698325",
                LocalDate.of(1990, 1, 1), "owner@plazoleta.com", "");

        assertThatThrownBy(() -> service.execute(noName)).isInstanceOf(AccountException.class);
        assertThatThrownBy(() -> service.execute(noPassword)).isInstanceOf(AccountException.class);
    }
}
