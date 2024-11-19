package ru.helper.worker.rest.external.create_order.model;

import java.time.LocalDateTime;

public record OrderCreateResponseDto(
        Long orderId,
        Long draftId,
        Long customerId,
        Long chatId,
        String category,
        String shortDescription,
        String detailedDescription,
        String status,
        LocalDateTime createdAt
) {
}
