package com.workers.config.resttemplate.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}
