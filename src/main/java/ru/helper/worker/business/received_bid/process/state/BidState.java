package ru.helper.worker.business.received_bid.process.state;

import ru.helper.worker.business.received_bid.process.context.BidContext;

public interface BidState {
    void handleInput(BidContext context, String input);
    void enter(BidContext context);
    void updateState(BidContext context);
}
