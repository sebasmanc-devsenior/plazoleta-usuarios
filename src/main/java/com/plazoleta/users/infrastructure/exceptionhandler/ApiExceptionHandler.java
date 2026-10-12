package com.plazoleta.users.infrastructure.exceptionhandler;

import com.plazoleta.users.domain.exceptions.AccountException;
import com.plazoleta.users.infrastructure.adapters.input.rest.InternalUsersController;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(AccountException.class)
    ResponseEntity<ErrorResponse> account(AccountException exception, HttpServletRequest request) {
        return response(HttpStatusCode.valueOf(exception.status()), exception.code(), exception.getMessage(), Map.of(), request);
    }

    @ExceptionHandler(InternalUsersController.InternalServiceAuthException.class)
    ResponseEntity<ErrorResponse> internalAuth(InternalUsersController.InternalServiceAuthException exception,
                                                HttpServletRequest request) {
        return response(exception.status(), exception.status() == HttpStatus.SERVICE_UNAVAILABLE
                ? "SERVICE_AUTH_NOT_CONFIGURED" : "INVALID_SERVICE_CREDENTIALS", exception.getMessage(), Map.of(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        Map<String, String> details = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> details.putIfAbsent(error.getField(), error.getDefaultMessage()));
        return response(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", "Request validation failed", details, request);
    }

    @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class})
    ResponseEntity<ErrorResponse> badRequest(Exception exception, HttpServletRequest request) {
        return response(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "Request is invalid", Map.of(), request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ErrorResponse> duplicate(DataIntegrityViolationException exception, HttpServletRequest request) {
        return response(HttpStatus.CONFLICT, "ACCOUNT_CONFLICT", "Email or identity document is already registered",
                Map.of(), request);
    }

    private ResponseEntity<ErrorResponse> response(HttpStatusCode status, String code, String message,
                                                    Map<String, String> details, HttpServletRequest request) {
        return ResponseEntity.status(status).body(new ErrorResponse(Instant.now(), status.value(), code,
                message, details, request.getRequestURI()));
    }
}
