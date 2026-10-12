package com.plazoleta.users.domain.exceptions;

public class AccountException extends RuntimeException {
    private final int status;
    private final String code;

    public AccountException(int status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public int status() { return status; }
    public String code() { return code; }

    public static AccountException conflict(String message) {
        return new AccountException(409, "ACCOUNT_CONFLICT", message);
    }

    public static AccountException unauthorized() {
        return new AccountException(401, "INVALID_CREDENTIALS", "Invalid email or password");
    }

    public static AccountException notFound() {
        return new AccountException(404, "USER_NOT_FOUND", "User was not found");
    }

    public static AccountException forbidden(String message) {
        return new AccountException(403, "FORBIDDEN", message);
    }

    public static AccountException restaurantNotFound() {
        return new AccountException(404, "RESTAURANT_NOT_FOUND", "Restaurant was not found");
    }

    public static AccountException dependencyUnavailable(String message) {
        return new AccountException(503, "DEPENDENCY_UNAVAILABLE", message);
    }

    public static AccountException badRequest(String message) {
        return new AccountException(400, "INVALID_REQUEST", message);
    }

    public static AccountException invalidEmail() {
        return new AccountException(400, "INVALID_EMAIL", "Email format is invalid");
    }

    public static AccountException invalidPhone() {
        return new AccountException(400, "INVALID_PHONE",
                "Phone must be at most 13 characters and may start with +");
    }

    public static AccountException invalidIdentityDocument() {
        return new AccountException(400, "INVALID_IDENTITY_DOCUMENT",
                "Identity document must contain only digits");
    }

    public static AccountException underage() {
        return new AccountException(400, "UNDERAGE_USER", "The user must be at least 18 years old");
    }
}
