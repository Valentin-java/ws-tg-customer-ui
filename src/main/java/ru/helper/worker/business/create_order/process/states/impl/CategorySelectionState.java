package ru.helper.worker.business.create_order.process.states.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.helper.worker.business.create_order.process.context.OrderContext;
import ru.helper.worker.controller.message.MessageService;
import ru.helper.worker.controller.model.enums.OrderCategory;
import ru.helper.worker.business.create_order.process.states.OrderState;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CategorySelectionState implements OrderState {

    private final ShortDescriptionState nextState;
    private final MessageService messageService;

    @Override
    public void handleInput(OrderContext context, String input) throws TelegramApiException {
        log.debug("Handling input in CategorySelectionState for chatId {}: {}", context.getChatId(), input);
        try {
            OrderCategory category = OrderCategory.valueOf(input);
            context.getOrderRequest().setCategory(category);
            context.setInputValid(true);
        } catch (IllegalArgumentException e) {
            messageService.sendMessage(context.getChatId(), "Пожалуйста, выберите категорию из предложенных вариантов.");
            context.setInputValid(false);
        }
    }

    @Override
    public void enter(OrderContext context) throws TelegramApiException {
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

    private void sendCategorySelection(OrderContext context) throws TelegramApiException {
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

        messageService.sendMessage(context.getChatId(), "Выберите категорию заказа:", keys);
    }
}
