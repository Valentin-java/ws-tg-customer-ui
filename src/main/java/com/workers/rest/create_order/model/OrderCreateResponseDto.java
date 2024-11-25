package com.workers.rest.create_order.model;

import java.time.LocalDateTime;

public record OrderCreateResponseDto(
        Long orderId,
        Long draftId,
        String customerId,
        Long chatId,
        String category,
        String shortDescription,
        String detailedDescription,
        String status,
        LocalDateTime createdAt
) {
}
