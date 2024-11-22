package com.workers.rest.external.bid.model;

public record FeedbackHandymanRequest(
        String customerId,
        Long orderId,
        Integer rating
) {
}
