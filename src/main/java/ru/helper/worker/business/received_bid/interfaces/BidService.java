package ru.helper.worker.business.received_bid.interfaces;

import ru.helper.worker.business.received_bid.model.BidReceiveRequest;

public interface BidService {

    void initProcess(BidReceiveRequest request);
}
