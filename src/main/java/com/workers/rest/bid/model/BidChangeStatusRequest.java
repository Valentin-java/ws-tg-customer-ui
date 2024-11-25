package com.workers.rest.bid.model;

public record BidChangeStatusRequest(
        Long orderId,
        Long bidId
) {
}
