package ru.helper.worker.business.received_bid.model;

import ru.helper.worker.business.received_bid.model.enums.BidStatus;

public record BidReceiveRequest(
        Long bidId,
        Long orderId,
        Long chatId,
        SpecialistOffer offer,
        BidStatus bidStatus
) {}
