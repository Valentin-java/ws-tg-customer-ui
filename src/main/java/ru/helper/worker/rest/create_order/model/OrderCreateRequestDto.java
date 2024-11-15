package ru.helper.worker.rest.create_order.model;

import java.math.BigDecimal;

public record OrderCreateRequestDto(
        String customerId,
        Integer category,
        String shortDescription,
        String detailedDescription,
        BigDecimal amount,
        String address
) {
}
