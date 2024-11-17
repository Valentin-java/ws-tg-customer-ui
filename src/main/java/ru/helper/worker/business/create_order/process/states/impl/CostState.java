package ru.helper.worker.business.create_order.process.states.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ru.helper.worker.business.create_order.process.context.OrderContext;
import ru.helper.worker.controller.events.MessageSendEvent;
import ru.helper.worker.business.create_order.process.states.OrderState;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class CostState implements OrderState {

    private static final String INCORRECT_INPUT = "Стоимость должна быть больше нуля. Пожалуйста, введите корректную сумму.";
    private static final String INCORRECT_INPUT_BY_NUMBERS = "Пожалуйста, введите корректную сумму.";
    private final AddressState nextState;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void handleInput(OrderContext context, String input) {
        log.debug("Handling input in CostState for chatId {}: {}", context.getChatId(), input);
        try {
            BigDecimal cost = new BigDecimal(input);
            if (cost.compareTo(BigDecimal.ZERO) <= 0) {
                eventPublisher.publishEvent(new MessageSendEvent(this, context.getChatId(), INCORRECT_INPUT));
                context.setInputValid(false);
            } else {
                context.getOrderRequest().setAmount(cost);
                context.setInputValid(true);
            }
        } catch (NumberFormatException e) {
            eventPublisher.publishEvent(new MessageSendEvent(this, context.getChatId(), INCORRECT_INPUT_BY_NUMBERS));
            context.setInputValid(false);
        }
    }

    @Override
    public void enter(OrderContext context) {
        eventPublisher.publishEvent(new MessageSendEvent(this, context.getChatId(), "Укажите стоимость:"));
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
