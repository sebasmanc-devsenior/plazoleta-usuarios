package com.plazoleta.users.domain.exception;

import com.plazoleta.users.domain.util.MensajesError;

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
        return new AccountException(401, "INVALID_CREDENTIALS", MensajesError.CORREO_O_CLAVE_INCORRECTOS);
    }

    public static AccountException notFound() {
        return new AccountException(404, "USER_NOT_FOUND", MensajesError.USUARIO_NO_EXISTE);
    }

    public static AccountException forbidden(String message) {
        return new AccountException(403, "FORBIDDEN", message);
    }

    public static AccountException restaurantNotFound() {
        return new AccountException(404, "RESTAURANT_NOT_FOUND", MensajesError.RESTAURANTE_NO_EXISTE);
    }

    public static AccountException dependencyUnavailable(String message) {
        return new AccountException(503, "DEPENDENCY_UNAVAILABLE", message);
    }

    public static AccountException badRequest(String message) {
        return new AccountException(400, "INVALID_REQUEST", message);
    }

    public static AccountException invalidEmail() {
        return new AccountException(400, "INVALID_EMAIL", MensajesError.FORMATO_DEL_CORREO_NO_ES_VALIDO);
    }

    public static AccountException invalidPhone() {
        return new AccountException(400, "INVALID_PHONE",
                MensajesError.CELULAR_DEBE_TENER_MAXIMO_13_CARACTERES_Y);
    }

    public static AccountException invalidIdentityDocument() {
        return new AccountException(400, "INVALID_IDENTITY_DOCUMENT",
                MensajesError.DOCUMENTO_DE_IDENTIDAD_SOLO_PUEDE_TENER_DIGITOS);
    }

    public static AccountException underage() {
        return new AccountException(400, "UNDERAGE_USER", MensajesError.USUARIO_DEBE_SER_MAYOR_DE_EDAD_18);
    }
}
