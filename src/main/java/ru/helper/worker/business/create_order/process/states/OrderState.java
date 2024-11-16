package ru.helper.worker.business.create_order.process.states;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.helper.worker.business.create_order.process.context.OrderContext;

public interface OrderState {
    void handleInput(OrderContext context, String input) throws TelegramApiException;
    void enter(OrderContext context) throws TelegramApiException;
    void updateState(OrderContext context);
}