package ru.helper.worker.business.create_order.process.states.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.helper.worker.business.create_order.process.context.OrderContext;
import ru.helper.worker.controller.events.MessageSendEvent;
import ru.helper.worker.controller.events.OrderProcessCompletedEvent;
import ru.helper.worker.controller.model.OrderRequest;
import ru.helper.worker.business.create_order.process.states.OrderState;
import ru.helper.worker.rest.common.OrderClientService;
import ru.helper.worker.rest.create_order.mapper.OrderMapper;
import ru.helper.worker.rest.create_order.model.OrderCreateRequestDto;
import ru.helper.worker.rest.create_order.model.OrderCreateResponseDto;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConfirmationState implements OrderState {

    private static final String SEND_ERROR_MSG = "Произошла ошибка при публикации заказа. Пожалуйста, попробуйте позже.";
    private static final String NOT_SENT_BY_USER_MSG = "Ваш заказ не был опубликован.";
    private static final String NOT_SELECTED_BY_USER_MSG = "Пожалуйста, выберите 'Да' или 'Нет' с помощью кнопок ниже.";

    private final OrderClientService<OrderCreateRequestDto, OrderCreateResponseDto> orderClient;
    private final ApplicationEventPublisher eventPublisher;
    private final OrderMapper mapper;

    @Override
    public void handleInput(OrderContext context, String input) throws TelegramApiException {
        log.debug("Handling input in ConfirmationState for chatId {}: {}", context.getChatId(), input);
        if ("CONFIRM_YES".equals(input)) {
            OrderCreateResponseDto result = orderClient.doRequest(mapper.toRequest(context.getOrderRequest()));
            if (result != null) {
                eventPublisher.publishEvent(new MessageSendEvent(this, context.getChatId(), "Ваш заказ опубликован! \n id заказа: " + result.orderId()));
            } else {
                eventPublisher.publishEvent(new MessageSendEvent(this, context.getChatId(), SEND_ERROR_MSG));
                // здесь надо прихранить черновик
            }
            updateState(context);
        } else if ("CONFIRM_NO".equals(input)) {
            eventPublisher.publishEvent(new MessageSendEvent(this, context.getChatId(), NOT_SENT_BY_USER_MSG));
            updateState(context);
        } else {
            eventPublisher.publishEvent(new MessageSendEvent(this, context.getChatId(), NOT_SELECTED_BY_USER_MSG));
        }
    }

    @Override
    public void enter(OrderContext context) {
        OrderRequest orderRequest = context.getOrderRequest();

        String summary = String.format(
                "Ваш заказ:\n\nКатегория: %s\nКраткое описание: %s\nДетальное описание: %s\nСтоимость: %s\nАдрес: %s\n\nОпубликовать заказ?",
                orderRequest.getCategory().getDescription(),
                orderRequest.getShortDescription(),
                orderRequest.getDetailedDescription(),
                orderRequest.getAmount(),
                orderRequest.getAddress()
        );

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text("Да")
                        .callbackData("CONFIRM_YES")
                        .build(),
                InlineKeyboardButton.builder()
                        .text("Нет")
                        .callbackData("CONFIRM_NO")
                        .build()
        ));

        InlineKeyboardMarkup keys = InlineKeyboardMarkup.builder().keyboard(buttons).build();

        eventPublisher.publishEvent(new MessageSendEvent(this, context.getChatId(), summary, keys));
    }

    @Override
    public void updateState(OrderContext context) {
        // Завершаем процесс
        eventPublisher.publishEvent(new OrderProcessCompletedEvent(this, context.getChatId()));
    }
}
