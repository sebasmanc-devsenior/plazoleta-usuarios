package com.plazoleta.users.infrastructure.input.rest.dto;

public record LoginResponse(String token, Long userId, String role) {}
