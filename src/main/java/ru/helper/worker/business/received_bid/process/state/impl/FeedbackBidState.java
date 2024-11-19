package ru.helper.worker.business.received_bid.process.state.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.helper.worker.business.received_bid.process.context.BidContext;
import ru.helper.worker.business.received_bid.process.state.BidState;
import ru.helper.worker.controller.events.MessageEditEvent;
import ru.helper.worker.controller.events.MessageSendEvent;
import ru.helper.worker.controller.events.OrderProcessCompletedEvent;
import ru.helper.worker.rest.external.bid.model.FeedbackHandymanRequest;
import ru.helper.worker.rest.external.common.ExternalClientService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Предлагаем оценить работу. 
 * Получаем оценку.
 * Отправляем в на сервер.
 * Редактируем сообщение с оценкой на благодарность.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FeedbackBidState implements BidState {

    private static final String RATING_REQUEST_MESSAGE = "На сколько вы остались довольны выполненной работой мастера:" +
            "\n\n5 - Очень доволен. Рекомендую." +
            "\n4 - Хорошо выполнил свою работу." +
            "\n3 - Удовлетворительно выполнил свою работу." +
            "\n2 - Очень плохо. Никому не пожелаю иметь с ним дело." +
            "\n1 - Были проблемы. Остался недоволен.";

    private static final String THANK_YOU_MESSAGE = "Спасибо за вашу оценку! Мы рады, что вы воспользовались нашим сервисом.";

    private final ExternalClientService<FeedbackHandymanRequest, ResponseEntity<Void>> clientService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void handleInput(BidContext context, String input) {
        try {
            int rating = Integer.parseInt(input); // Парсим рейтинг из callbackData
            saveRating(context, rating); // Сохраняем рейтинг
            sendThankYouMessage(context); // Отправляем сообщение благодарности
            updateState(context);
        } catch (NumberFormatException e) {
            log.error("Invalid input received for rating: {}", input);
        }
    }

    @Override
    public void enter(BidContext context) {
        InlineKeyboardMarkup keys = createRatingButtons();

        var messageEvent = new MessageSendEvent(this, context.getChatId(), RATING_REQUEST_MESSAGE, keys);
        var messageId = getMessageIdAfterPublishMessage(messageEvent);
        context.setMessageIdActualState(messageId);
    }

    @Override
    public void updateState(BidContext context) {
        eventPublisher.publishEvent(new OrderProcessCompletedEvent(this, context.getChatId()));
    }

    private InlineKeyboardMarkup createRatingButtons() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        for (int i = 5; i >= 1; i--) {
            buttons.add(List.of(
                    InlineKeyboardButton.builder()
                            .text(String.valueOf(i)) // Текст кнопки - число
                            .callbackData(String.valueOf(i)) // CallbackData - рейтинг
                            .build()
            ));
        }

        return InlineKeyboardMarkup.builder().keyboard(buttons).build();
    }

    private void saveRating(BidContext context, int rating) {
        log.info("Rating {} received for chatId: {}", rating, context.getChatId());
        var request = new FeedbackHandymanRequest(
                context.getRequest().offer().specialistId(),
                context.getRequest().orderId(), 
                rating);
        
        clientService.doRequest(request);
    }

    private void sendThankYouMessage(BidContext context) {
        Integer messageId = context.getMessageIdActualState();
        if (messageId != null) {
            eventPublisher.publishEvent(new MessageEditEvent(this, context.getChatId(), messageId, THANK_YOU_MESSAGE));
        }
    }

    public Integer getMessageIdAfterPublishMessage(MessageSendEvent event) {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        event.setCallback(future::complete);
        eventPublisher.publishEvent(event);
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException("Error getting messageId from event", e);
        }
    }
}
