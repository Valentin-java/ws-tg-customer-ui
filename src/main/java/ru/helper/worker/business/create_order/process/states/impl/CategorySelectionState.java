package ru.helper.worker.business.create_order.process.states.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.helper.worker.business.create_order.process.context.OrderContext;
import ru.helper.worker.controller.events.MessageSendEvent;
import ru.helper.worker.business.create_order.model.enums.OrderCategory;
import ru.helper.worker.business.create_order.process.states.OrderState;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CategorySelectionState implements OrderState {

    private static final String NOT_SELECTED_MSG = "Пожалуйста, выберите категорию из предложенных вариантов.";

    private final ShortDescriptionState nextState;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void handleInput(OrderContext context, String input) {
        log.debug("Handling input in CategorySelectionState for chatId {}: {}", context.getChatId(), input);
        try {
            OrderCategory category = OrderCategory.valueOf(input);
            context.getOrderRequest().setCategory(category);
            context.setInputValid(true);
        } catch (IllegalArgumentException e) {
            eventPublisher.publishEvent(new MessageSendEvent(this, context.getChatId(), NOT_SELECTED_MSG));
            context.setInputValid(false);
        }
    }

    @Override
    public void enter(OrderContext context) {
        sendCategorySelection(context);
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

    private void sendCategorySelection(OrderContext context) {
        // Создаем инлайн-кнопки для категорий
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (OrderCategory category : OrderCategory.values()) {
            buttons.add(List.of(
                    InlineKeyboardButton.builder()
                            .text(category.getDescription())
                            .callbackData(category.name())
                            .build()
            ));
        }

        InlineKeyboardMarkup keys = InlineKeyboardMarkup.builder().keyboard(buttons).build();

        eventPublisher.publishEvent(new MessageSendEvent(this, context.getChatId(), NOT_SELECTED_MSG, keys));
    }
}
