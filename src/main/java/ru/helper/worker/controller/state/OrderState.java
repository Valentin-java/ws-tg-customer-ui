package ru.helper.worker.controller.state;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.helper.worker.controller.context.OrderContext;

public interface OrderState {
    void handleInput(OrderContext context, String input) throws TelegramApiException;
    void enter(OrderContext context) throws TelegramApiException;
    void updateState(OrderContext context);
}
