package com.workers.business.received_bid.process.state.impl;

import com.workers.business.received_bid.model.enums.BidReceivePayloadEnum;
import com.workers.business.received_bid.process.context.BidContext;
import com.workers.business.received_bid.process.state.BidState;
import com.workers.controller.events.MessageEditEvent;
import com.workers.controller.events.MessageSendEvent;
import com.workers.controller.events.OrderProcessCompletedEvent;
import com.workers.rest.external.bid.interfaces.RejectBidClient;
import com.workers.rest.external.bid.model.BidChangeStatusRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AcceptedBidState implements BidState {

    private static final String NOTICE_MESSAGE = "Похоже что предложение данного мастера Вас не заинтересовало. " +
            "\n Будем искать дальше.";
    private static final String WARNING_MESSAGE = "Похоже что в процессе произошла ошибка. В любом случае мы отменили заявку. " +
            "\nПри необходимости обратитесь в поддержку.";

    private final RejectBidClient rejectBidClient;
    private final ApplicationEventPublisher eventPublisher;
    private final CompletedBidState nextState;

    @Override
    public void handleInput(BidContext context, String input) {
        if (BidReceivePayloadEnum.SUCCESS_BID.name().equals(input)) {
            updateState(context);
            return;
        }
        if (BidReceivePayloadEnum.REJECT_BID.name().equals(input)) {
            var orderId = context.getRequest().orderId();
            var bidId = context.getRequest().bidId();

            var request = new BidChangeStatusRequest(orderId, bidId);
            var response = rejectBidClient.doRequest(request);

            if (response != null && response.hasBody() && response.getStatusCode().is2xxSuccessful()) {
                log.warn("Success to process reject bid order with ID: {}", orderId);
                eventPublisher.publishEvent(new MessageEditEvent(this, context.getChatId(), context.getMessageIdActualState(), NOTICE_MESSAGE));
            } else {
                eventPublisher.publishEvent(new MessageEditEvent(this, context.getChatId(), context.getMessageIdActualState(), WARNING_MESSAGE));
            }
            eventPublisher.publishEvent(new OrderProcessCompletedEvent(this, context.getChatId()));

        }

    }

    @Override
    public void enter(BidContext context) {
        String summary = "Мы рады, что Вы смогли договорится с мастером. " +
                "\n Будем с нетерпением ждать успешного завершения Вашего заказа.";

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(getUpperRowInlineButtons());

        InlineKeyboardMarkup keys = InlineKeyboardMarkup.builder().keyboard(buttons).build();
        eventPublisher.publishEvent(new MessageSendEvent(this, context.getChatId(), summary, keys));
    }

    @Override
    public void updateState(BidContext context) {
        context.setCurrentState(nextState);
        context.getCurrentState().enter(context);
    }

    private List<InlineKeyboardButton> getUpperRowInlineButtons() {
        return List.of(InlineKeyboardButton.builder()
                        .text("Мастер выполнил работу!")
                        .callbackData(BidReceivePayloadEnum.SUCCESS_BID.name())
                        .build(),
                InlineKeyboardButton.builder()
                        .text("Отказаться от мастера")
                        .callbackData(BidReceivePayloadEnum.REJECT_BID.name())
                        .build());
    }
}
