package com.workers.business.received_bid.process.state.impl;

import com.workers.business.received_bid.model.enums.BidReceivePayloadEnum;
import com.workers.business.received_bid.process.context.BidContext;
import com.workers.business.received_bid.process.state.BidState;
import com.workers.controller.events.MessageEditEvent;
import com.workers.controller.events.MessageSendEvent;
import com.workers.controller.events.OrderProcessCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompletedBidState implements BidState {

    private static final String CONGRATS_MESSAGE = "Мы искрине рады тому, что Вы смогли решить свои вопросы с помощью нашего сервиса. " +
            "\n Будем ждать Вас снова.";
    private final ApplicationEventPublisher eventPublisher;
    private final FeedbackBidState nextState;

    @Override
    public void handleInput(BidContext context, String input) {
        if (BidReceivePayloadEnum.REJECT_FEEDBACK_BID.name().equals(input)) {
            setCongratsMessage(context);
            eventPublisher.publishEvent(new OrderProcessCompletedEvent(this, context.getChatId()));
            return;
        }
        if (BidReceivePayloadEnum.FEEDBACK_BID.name().equals(input)) {
            updateState(context);
        }
    }

    @Override
    public void enter(BidContext context) {

        String summary = "Вы только что завершили свой заказ. " +
                "\n Надеемся Вы удовлетворены проделанной работой мастера." +
                "\n Мы были бы признательны, если Вы оставите небольшой, " +
                "лаконичный отзыв о проделанной работе мастером.";

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(getUpperRowInlineButtons());

        InlineKeyboardMarkup keys = InlineKeyboardMarkup.builder().keyboard(buttons).build();
        var messageEvent = new MessageSendEvent(this, context.getChatId(), summary, keys);
        var messageId = getMessageIdAfterPublishMessage(messageEvent);
        context.setMessageIdActualState(messageId);
    }

    @Override
    public void updateState(BidContext context) {
        context.setCurrentState(nextState);
        context.getCurrentState().enter(context);
    }

    private List<InlineKeyboardButton> getUpperRowInlineButtons() {
        return List.of(InlineKeyboardButton.builder()
                        .text("Оставить отзыв")
                        .callbackData(BidReceivePayloadEnum.FEEDBACK_BID.name())
                        .build(),
                InlineKeyboardButton.builder()
                        .text("Не оставлять отзыв")
                        .callbackData(BidReceivePayloadEnum.REJECT_FEEDBACK_BID.name())
                        .build());
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

    private void setCongratsMessage(BidContext context) {
        Integer messageId = context.getMessageIdActualState();
        if (messageId != null) {
            eventPublisher.publishEvent(new MessageEditEvent(this, context.getChatId(), messageId, CONGRATS_MESSAGE));
        }
    }
}
