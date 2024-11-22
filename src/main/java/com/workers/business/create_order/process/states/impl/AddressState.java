package com.workers.business.create_order.process.states.impl;

import com.workers.business.create_order.process.context.OrderContext;
import com.workers.business.create_order.process.states.OrderState;
import com.workers.controller.events.MessageSendEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddressState implements OrderState {

    private static final String EMPTY_ADDRESS_MSG = "Адрес не может быть пустым. Пожалуйста, введите адрес.";

    private final ConfirmationState nextState;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void handleInput(OrderContext context, String input) {
        log.debug("Handling input in AddressState for chatId {}: {}", context.getChatId(), input);
        if (input.trim().isEmpty()) {
            eventPublisher.publishEvent(new MessageSendEvent(this, context.getChatId(), EMPTY_ADDRESS_MSG));
            context.setInputValid(false);
        } else {
            context.getOrderRequest().setAddress(input);
            context.setInputValid(true);
        }
    }

    @Override
    public void enter(OrderContext context) {
        eventPublisher.publishEvent(new MessageSendEvent(this, context.getChatId(), "Укажите адрес:"));
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
