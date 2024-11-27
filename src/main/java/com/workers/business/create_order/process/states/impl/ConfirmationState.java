package com.workers.business.create_order.process.states.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import com.workers.business.create_order.process.context.OrderContext;
import com.workers.controller.events.MessageSendEvent;
import com.workers.controller.events.OrderProcessCompletedEvent;
import com.workers.business.create_order.model.OrderRequest;
import com.workers.business.create_order.process.states.OrderState;
import com.workers.persistence.mapper.DraftOrderMapper;
import com.workers.persistence.enums.OrderStatus;
import com.workers.persistence.enums.SendProcess;
import com.workers.persistence.repository.DraftOrderRepository;
import com.workers.rest.create_order.interfaces.CreateOrderClient;
import com.workers.rest.create_order.mapper.OrderMapper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConfirmationState implements OrderState {

    private static final String SEND_ERROR_MSG = "Произошла ошибка при публикации заказа, возможно какие-то проблемы с сервисом. " +
            "\n Мы сохранили Ваш заказ, чтобы отправить его позже. \n Мы отправим Вам уведомление при успешной публикации заказа.";
    private static final String NOT_SENT_BY_USER_MSG = "Ваш заказ не был опубликован. \n Ваш заказ был удален.";
    private static final String NOT_SELECTED_BY_USER_MSG = "Пожалуйста, выберите 'Да' или 'Нет' с помощью кнопок ниже.";
    private static final String SENT_BY_SUCCESS_MSG = "Ваш заказ опубликован! \n id заказа: %s";

    private final CreateOrderClient createOrderClient;
    private final ApplicationEventPublisher eventPublisher;
    private final DraftOrderRepository orderRepository;
    private final DraftOrderMapper draftOrderMapper;
    private final OrderMapper mapper;

    @Override
    public void handleInput(OrderContext context, String input) {
        log.debug("Handling input in ConfirmationState for chatId {}: {}", context.getChatId(), input);
        if ("CONFIRM_YES".equals(input)) {

            var order = mapper.toRequest(context.getOrderRequest());
            var response = createOrderClient.doRequest(order);

            if (response.getStatusCode().is2xxSuccessful()) {
                eventPublisher.publishEvent(new MessageSendEvent(this, context.getChatId(), String.format(SENT_BY_SUCCESS_MSG, response.getBody().orderId())));
            } else {
                var draftOrder = draftOrderMapper.toEntity(context);
                draftOrder.setStatus(OrderStatus.DRAFT);
                draftOrder.setSendProcess(SendProcess.AUTO);
                orderRepository.save(draftOrder);
                eventPublisher.publishEvent(new MessageSendEvent(this, context.getChatId(), SEND_ERROR_MSG));
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
                "Ваш заказ:" +
                        "\n\nКатегория: %s" +
                        "\nКраткое описание: %s" +
                        "\nДетальное описание: %s" +
                        "\nСтоимость: %s" +
                        "\nАдрес: %s" +
                        "\n\nОпубликовать заказ?",
                orderRequest.getCategory().getDescription(),
                orderRequest.getShortDescription(),
                orderRequest.getDetailedDescription(),
                orderRequest.getAmount(),
                orderRequest.getAddress()
        );

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(getUpperRowInlineButtons());

        InlineKeyboardMarkup keys = InlineKeyboardMarkup.builder().keyboard(buttons).build();
        eventPublisher.publishEvent(new MessageSendEvent(this, context.getChatId(), summary, keys));
    }

    @Override
    public void updateState(OrderContext context) {
        // Завершаем процесс
        eventPublisher.publishEvent(new OrderProcessCompletedEvent(this, context.getChatId()));
    }

    private List<InlineKeyboardButton> getUpperRowInlineButtons() {
        return List.of(
                InlineKeyboardButton.builder()
                        .text("Отправить")
                        .callbackData("CONFIRM_YES")
                        .build(),
                InlineKeyboardButton.builder()
                        .text("Удалить")
                        .callbackData("CONFIRM_NO")
                        .build());
    }
}
