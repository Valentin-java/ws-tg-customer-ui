package com.workers.business.received_bid.interfaces;

import com.workers.business.received_bid.model.BidReceiveRequest;

public interface BidService {

    void initProcess(BidReceiveRequest request);
}
