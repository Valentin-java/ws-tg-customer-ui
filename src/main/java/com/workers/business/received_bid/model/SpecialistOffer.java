package com.workers.business.received_bid.model;

import java.math.BigDecimal;

public record SpecialistOffer(
        String specialistId,
        String specialistName,
        String message,
        BigDecimal price
) {
}
