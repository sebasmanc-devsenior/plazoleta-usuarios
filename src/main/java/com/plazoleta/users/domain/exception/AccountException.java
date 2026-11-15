package com.plazoleta.users.domain.exception;

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
        return new AccountException(401, "INVALID_CREDENTIALS", "Correo o clave incorrectos");
    }

    public static AccountException notFound() {
        return new AccountException(404, "USER_NOT_FOUND", "El usuario no existe");
    }

    public static AccountException forbidden(String message) {
        return new AccountException(403, "FORBIDDEN", message);
    }

    public static AccountException restaurantNotFound() {
        return new AccountException(404, "RESTAURANT_NOT_FOUND", "El restaurante no existe");
    }

    public static AccountException dependencyUnavailable(String message) {
        return new AccountException(503, "DEPENDENCY_UNAVAILABLE", message);
    }

    public static AccountException badRequest(String message) {
        return new AccountException(400, "INVALID_REQUEST", message);
    }

    public static AccountException invalidEmail() {
        return new AccountException(400, "INVALID_EMAIL", "El formato del correo no es válido");
    }

    public static AccountException invalidPhone() {
        return new AccountException(400, "INVALID_PHONE",
                "El celular debe tener máximo 13 caracteres y puede empezar con +");
    }

    public static AccountException invalidIdentityDocument() {
        return new AccountException(400, "INVALID_IDENTITY_DOCUMENT",
                "El documento de identidad solo puede tener dígitos");
    }

    public static AccountException underage() {
        return new AccountException(400, "UNDERAGE_USER", "El usuario debe ser mayor de edad (18 años o más)");
    }
}
