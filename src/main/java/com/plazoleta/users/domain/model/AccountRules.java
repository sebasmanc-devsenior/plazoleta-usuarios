package com.plazoleta.users.domain.model;

import com.plazoleta.users.domain.exception.AccountException;
import com.plazoleta.users.domain.model.UserRole;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public final class AccountRules {
    public static final int PHONE_MAX_LENGTH = 13;
    public static final int MIN_ADULT_AGE = 18;
    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern IDENTITY_DOCUMENT = Pattern.compile("\\d+");
    private static final Pattern PHONE = Pattern.compile("(?:\\+\\d{7,12}|\\d{7,13})");

    private AccountRules() {
    }

    public static void validateNewAccount(String name, String lastName, String identityDocument, String phone,
                                          LocalDate birthDate, String email, String password, UserRole role,
                                          Long restaurantId) {
        if (name == null || name.isBlank()) {
            throw AccountException.badRequest("Name is required");
        }
        if (lastName == null || lastName.isBlank()) {
            throw AccountException.badRequest("Last name is required");
        }
        if (identityDocument == null || !IDENTITY_DOCUMENT.matcher(identityDocument.trim()).matches()) {
            throw AccountException.invalidIdentityDocument();
        }
        if (email == null || !EMAIL.matcher(email.trim()).matches()) {
            throw AccountException.invalidEmail();
        }
        if (password == null || password.isBlank()) {
            throw AccountException.badRequest("Password is required");
        }
        if (role == null) {
            throw AccountException.badRequest("Role is required");
        }
        if (role != UserRole.ADMIN) {
            if (phone == null || phone.isBlank() || phone.length() > PHONE_MAX_LENGTH
                    || !PHONE.matcher(phone.trim()).matches()) {
                throw AccountException.invalidPhone();
            }
        } else if (phone != null && !phone.isBlank()
                && (phone.length() > PHONE_MAX_LENGTH || !PHONE.matcher(phone.trim()).matches())) {
            throw AccountException.invalidPhone();
        }
        if (role == UserRole.PROPIETARIO) {
            requireAdult(birthDate);
        }
        if ((role == UserRole.EMPLEADO) != (restaurantId != null)) {
            throw AccountException.badRequest("restaurantId is required only for EMPLEADO accounts");
        }
    }

    public static void requireAdult(LocalDate birthDate) {
        if (birthDate == null) {
            throw AccountException.underage();
        }
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age < MIN_ADULT_AGE) {
            throw AccountException.underage();
        }
    }
}
