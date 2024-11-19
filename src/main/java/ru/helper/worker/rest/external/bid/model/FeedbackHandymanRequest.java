package ru.helper.worker.rest.external.bid.model;

public record FeedbackHandymanRequest(
        Long customerId,
        Long orderId,
        Integer rating
) {
}
