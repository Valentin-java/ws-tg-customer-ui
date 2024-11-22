package com.workers.business.create_order.process.states.impl;

import com.workers.controller.events.MessageSendEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import com.workers.business.create_order.process.context.OrderContext;
import com.workers.business.create_order.process.states.OrderState;

@Slf4j
@Component
@RequiredArgsConstructor
public class DetailedDescriptionState implements OrderState {

    private static final String INCORRECT_INPUT = "Детальное описание не может быть пустым. Пожалуйста, введите описание.";

    private final CostState nextState;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void handleInput(OrderContext context, String input) {
        log.debug("Handling input in DetailedDescriptionState for chatId {}: {}", context.getChatId(), input);
        if (input.trim().isEmpty()) {
            eventPublisher.publishEvent(new MessageSendEvent(this, context.getChatId(), INCORRECT_INPUT));
            context.setInputValid(false);
        } else {
            context.getOrderRequest().setDetailedDescription(input);
            context.setInputValid(true);
        }
    }

    @Override
    public void enter(OrderContext context) {
        eventPublisher.publishEvent(new MessageSendEvent(this, context.getChatId(), "Теперь напишите более подробно о предстоящей работе:"));
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
