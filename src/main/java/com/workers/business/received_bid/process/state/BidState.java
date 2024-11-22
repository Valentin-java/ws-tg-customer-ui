package com.workers.business.received_bid.process.state;

import com.workers.business.received_bid.process.context.BidContext;

public interface BidState {
    void handleInput(BidContext context, String input);
    void enter(BidContext context);
    void updateState(BidContext context);
}
