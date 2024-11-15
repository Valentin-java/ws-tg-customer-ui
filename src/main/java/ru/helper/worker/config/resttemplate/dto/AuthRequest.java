package ru.helper.worker.config.resttemplate.dto;

public record AuthRequest(
        String username,
        String password,
        Boolean otp
) {
}
