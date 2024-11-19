package ru.helper.worker.business.received_bid.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.helper.worker.business.common.manager.UserContextManager;
import ru.helper.worker.business.received_bid.interfaces.BidService;
import ru.helper.worker.business.received_bid.model.BidReceiveRequest;
import ru.helper.worker.business.received_bid.process.context.BidContext;
import ru.helper.worker.business.received_bid.process.state.BidState;

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
