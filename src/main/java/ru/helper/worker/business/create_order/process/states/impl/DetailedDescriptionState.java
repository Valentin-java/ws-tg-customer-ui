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
public class DetailedDescriptionState implements OrderState {

    private final CostState nextState;
    private final MessageService messageService;

    @Override
    public void handleInput(OrderContext context, String input) throws TelegramApiException {
        log.debug("Handling input in DetailedDescriptionState for chatId {}: {}", context.getChatId(), input);
        if (input.trim().isEmpty()) {
            messageService.sendMessage(context.getChatId(), "Детальное описание не может быть пустым. Пожалуйста, введите описание.");
            context.setInputValid(false);
        } else {
            context.getOrderRequest().setDetailedDescription(input);
            context.setInputValid(true);
        }
    }

    @Override
    public void enter(OrderContext context) throws TelegramApiException {
        messageService.sendMessage(context.getChatId(), "Теперь напишите более подробно о предстоящей работе:");
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
