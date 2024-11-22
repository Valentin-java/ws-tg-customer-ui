package com.workers.business.received_bid.service;

import com.workers.business.common.manager.UserContextManager;
import com.workers.business.received_bid.interfaces.BidService;
import com.workers.business.received_bid.model.BidReceiveRequest;
import com.workers.business.received_bid.process.context.BidContext;
import com.workers.business.received_bid.process.state.BidState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BidServiceImpl implements BidService {

    private final UserContextManager contextManager;
    private final BidState initBidState;

    public BidServiceImpl(UserContextManager contextManager,
                          @Qualifier("bidReceiveState") BidState initBidState) {
        this.contextManager = contextManager;
        this.initBidState = initBidState;
    }

    @Override
    public void initProcess(BidReceiveRequest request) {
        BidContext context = new BidContext(request.chatId());
        context.setCurrentState(initBidState);
        context.setRequest(request);
        contextManager.addContext(request.chatId(), context);
        context.getCurrentState().enter(context);
    }
}
