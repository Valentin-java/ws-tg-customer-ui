package ru.helper.worker.rest.create_order.model;

import java.time.LocalDateTime;

public record OrderCreateResponseDto(
        Long orderId,
        Long customerId,
        String category,
        String shortDescription,
        String detailedDescription,
        String status,
        LocalDateTime createdAt
) {
}
