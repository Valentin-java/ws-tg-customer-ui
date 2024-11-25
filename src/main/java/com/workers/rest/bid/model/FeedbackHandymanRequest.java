package com.workers.rest.bid.model;

public record FeedbackHandymanRequest(
        String customerId,
        Long orderId,
        Integer rating
) {
}
