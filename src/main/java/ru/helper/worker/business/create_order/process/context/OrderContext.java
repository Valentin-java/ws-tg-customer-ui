package ru.helper.worker.business.create_order.process.context;

import lombok.Data;
import ru.helper.worker.controller.process.GenericContext;
import ru.helper.worker.controller.model.OrderRequest;
import ru.helper.worker.business.create_order.process.states.OrderState;

import java.time.LocalDateTime;

@Data
public class OrderContext implements GenericContext {
    private Long chatId;
    private OrderState currentState;
    private OrderRequest orderRequest;
    private boolean isInputValid;
    private LocalDateTime contextCreated;

    public OrderContext(Long chatId) {
        this.chatId = chatId;
        this.orderRequest = new OrderRequest();
        this.contextCreated = LocalDateTime.now();
    }

    @Override
    public boolean isActive() {
        return currentState != null;
    }

    @Override
    public void continueProcess(String input) {
        currentState.handleInput(this, input);
        currentState.updateState(this);
    }
}
