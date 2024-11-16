package ru.helper.worker.business.create_order.process.states.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.helper.worker.business.create_order.process.context.OrderContext;
import ru.helper.worker.controller.message.MessageService;
import ru.helper.worker.business.create_order.process.states.OrderState;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddressState implements OrderState {

    private final ConfirmationState nextState;
    private final MessageService messageService;

    @Override
    public void handleInput(OrderContext context, String input) throws TelegramApiException {
        log.debug("Handling input in AddressState for chatId {}: {}", context.getChatId(), input);
        if (input.trim().isEmpty()) {
            messageService.sendMessage(context.getChatId(), "Адрес не может быть пустым. Пожалуйста, введите адрес.");
            context.setInputValid(false);
        } else {
            context.getOrderRequest().setAddress(input);
            context.setInputValid(true);
        }
    }

    @Override
    public void enter(OrderContext context) throws TelegramApiException {
        messageService.sendMessage(context.getChatId(), "Укажите адрес:");
    }

    @Override
    @SneakyThrows
    public void updateState(OrderContext context) {
        if (context.isInputValid()) {
            context.setCurrentState(nextState);
            context.getCurrentState().enter(context);
        } else {
            enter(context);
        }
    }
}
