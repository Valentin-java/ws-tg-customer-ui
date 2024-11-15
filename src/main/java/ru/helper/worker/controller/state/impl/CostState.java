package ru.helper.worker.controller.state.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.helper.worker.controller.context.OrderContext;
import ru.helper.worker.controller.message.MessageService;
import ru.helper.worker.controller.state.OrderState;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class CostState implements OrderState {

    private final AddressState nextState;
    private final MessageService messageService;

    @Override
    public void handleInput(OrderContext context, String input) throws TelegramApiException {
        log.debug("Handling input in CostState for chatId {}: {}", context.getChatId(), input);
        try {
            BigDecimal cost = new BigDecimal(input);
            if (cost.compareTo(BigDecimal.ZERO) <= 0) {
                messageService.sendMessage(context.getChatId(), "Стоимость должна быть больше нуля. Пожалуйста, введите корректную сумму.");
                context.setInputValid(false);
            } else {
                context.getOrderRequest().setAmount(cost);
                context.setInputValid(true);
            }
        } catch (NumberFormatException e) {
            messageService.sendMessage(context.getChatId(), "Пожалуйста, введите корректную сумму.");
            context.setInputValid(false);
        }
    }

    @Override
    public void enter(OrderContext context) throws TelegramApiException {
        messageService.sendMessage(context.getChatId(), "Укажите стоимость:");
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
