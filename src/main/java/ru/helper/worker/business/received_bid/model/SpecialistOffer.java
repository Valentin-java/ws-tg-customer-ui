package ru.helper.worker.business.received_bid.model;

import java.math.BigDecimal;

public record SpecialistOffer(
        Long specialistId,
        String specialistName,
        String message,
        BigDecimal price
) {
}