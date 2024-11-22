package com.workers.business.received_bid.process.context;

import com.workers.business.received_bid.model.BidReceiveRequest;
import lombok.Data;
import com.workers.business.common.context.GenericContext;
import com.workers.business.received_bid.process.state.BidState;

import java.time.LocalDateTime;

@Data
public class BidContext implements GenericContext {

    private Long chatId;
    private BidState currentState;
    private BidReceiveRequest request;
    private boolean isInputValid;
    private LocalDateTime contextCreated;
    private Integer messageIdActualState;
    private Integer bidRating;

    public BidContext(Long chatId) {
        this.chatId = chatId;
        this.contextCreated = LocalDateTime.now();
    }

    @Override
    public boolean isActive() {
        return currentState != null;
    }

    @Override
    public void continueProcess(String input) {
        currentState.handleInput(this, input);
    }
}
