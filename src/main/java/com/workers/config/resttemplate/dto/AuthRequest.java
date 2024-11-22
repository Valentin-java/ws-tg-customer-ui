package com.workers.config.resttemplate.dto;

public record AuthRequest(
        String username,
        String password,
        Boolean otp
) {
}
