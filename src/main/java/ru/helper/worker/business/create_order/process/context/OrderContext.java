package ru.helper.worker.business.create_order.process.context;

import lombok.Data;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.helper.worker.controller.process.UserContext;
import ru.helper.worker.controller.model.OrderRequest;
import ru.helper.worker.business.create_order.process.states.OrderState;

@Data
public class OrderContext implements UserContext {
    private Long chatId;
    private OrderState currentState;
    private OrderRequest orderRequest;
    private boolean isInputValid;

    public OrderContext(Long chatId) {
        this.chatId = chatId;
        this.orderRequest = new OrderRequest();
    }

    @Override
    public boolean isActive() {
        return currentState != null;
    }

    @Override
    public void continueProcess(String input) throws TelegramApiException {
        currentState.handleInput(this, input);
        currentState.updateState(this);
    }
}
