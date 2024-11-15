package ru.helper.worker.config.resttemplate.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}
