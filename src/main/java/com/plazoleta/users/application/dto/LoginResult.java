package com.plazoleta.users.application.dto;

public record LoginResult(String token, Long userId, String role) {
}
