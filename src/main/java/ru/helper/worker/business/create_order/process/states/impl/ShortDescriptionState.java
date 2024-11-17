package ru.helper.worker.business.create_order.process.states.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ru.helper.worker.business.create_order.process.context.OrderContext;
import ru.helper.worker.business.create_order.process.states.OrderState;
import ru.helper.worker.controller.events.MessageSendEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShortDescriptionState implements OrderState {

    private static final String INCORRECT_INPUT = "Краткое описание не может быть пустым. Пожалуйста, введите описание.";

    private final DetailedDescriptionState nextState;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void handleInput(OrderContext context, String input) {
        log.debug("Handling input in ShortDescriptionState for chatId {}: {}", context.getChatId(), input);
        if (input.trim().isEmpty()) {
            eventPublisher.publishEvent(new MessageSendEvent(this, context.getChatId(), INCORRECT_INPUT));
            context.setInputValid(false);
        } else {
            context.getOrderRequest().setShortDescription(input);
            context.setInputValid(true);
        }
    }

    @Override
    public void enter(OrderContext context) {
        eventPublisher.publishEvent(new MessageSendEvent(this, context.getChatId(), "Напишите кратко, что нужно сделать:"));
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
