package com.workers.rest.external.bid.model;

public record BidChangeStatusRequest(
        Long orderId,
        Long bidId
) {
}
