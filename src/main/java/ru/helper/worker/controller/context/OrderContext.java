package ru.helper.worker.controller.context;

import lombok.Data;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.helper.worker.controller.model.OrderRequest;
import ru.helper.worker.controller.state.OrderState;

@Data
public class OrderContext {
    private Long chatId;
    private OrderState currentState;
    private OrderRequest orderRequest;
    private boolean isInputValid;

    public OrderContext(Long chatId) {
        this.chatId = chatId;
        this.orderRequest = new OrderRequest();
    }

    public void continueProcessInput(String input) throws TelegramApiException {
        currentState.handleInput(this, input);
        currentState.updateState(this);
    }
}
