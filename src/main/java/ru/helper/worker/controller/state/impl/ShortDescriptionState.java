package ru.helper.worker.controller.state.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.helper.worker.controller.context.OrderContext;
import ru.helper.worker.controller.message.MessageService;
import ru.helper.worker.controller.state.OrderState;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShortDescriptionState implements OrderState {

    private final DetailedDescriptionState nextState;
    private final MessageService messageService;

    @Override
    public void handleInput(OrderContext context, String input) throws TelegramApiException {
        log.debug("Handling input in ShortDescriptionState for chatId {}: {}", context.getChatId(), input);
        if (input.trim().isEmpty()) {
            messageService.sendMessage(context.getChatId(), "Краткое описание не может быть пустым. Пожалуйста, введите описание.");
            context.setInputValid(false);
        } else {
            context.getOrderRequest().setShortDescription(input);
            context.setInputValid(true);
        }
    }

    @Override
    public void enter(OrderContext context) throws TelegramApiException {
        messageService.sendMessage(context.getChatId(), "Напишите кратко, что нужно сделать:");
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
