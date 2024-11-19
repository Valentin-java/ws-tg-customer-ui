package ru.helper.worker.rest.external.bid.model;

public record BidChangeStatusRequest(
        Long orderId,
        Long bidId
) {
}
