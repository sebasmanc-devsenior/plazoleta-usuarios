package com.plazoleta.users.infrastructure.adapters.input.rest.dto;

public record LoginResponse(String token, Long userId, String role) {}
