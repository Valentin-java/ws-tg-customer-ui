package com.workers.rest.create_order.model;

import java.math.BigDecimal;

public record OrderCreateRequest(
        Long draftId,
        String customerId,
        Long chatId,
        Integer category,
        String shortDescription,
        String detailedDescription,
        BigDecimal amount,
        String address
) {
}
