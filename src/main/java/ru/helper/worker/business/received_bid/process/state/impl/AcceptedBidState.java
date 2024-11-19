package ru.helper.worker.business.received_bid.process.state.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.helper.worker.business.received_bid.process.context.BidContext;
import ru.helper.worker.business.received_bid.process.state.BidState;
import ru.helper.worker.business.received_bid.strategy.BidCasesStrategy;
import ru.helper.worker.controller.events.MessageSendEvent;
import ru.helper.worker.controller.events.OrderProcessCompletedEvent;

import java.util.ArrayList;
import java.util.List;

import static ru.helper.worker.business.received_bid.model.enums.BidReceivePayloadEnum.REJECT_BID;
import static ru.helper.worker.business.received_bid.model.enums.BidReceivePayloadEnum.SUCCESS_BID;

// Здесь поздравим с успешным завершением поиска специалиста, предложим нажать на кнопку завершить
// когда исполнитель завершит работы, затем предложим написать отзыв о проделанной работе
@Slf4j
@Component
@RequiredArgsConstructor
public class AcceptedBidState implements BidState {

    private static final String NOTICE_MESSAGE = "Похоже что предложение данного мастера Вас не устроило. " +
            "\n Будем искать дальше.";

    private final ApplicationEventPublisher eventPublisher;
    private final CompletedBidState nextState;

    @Override
    public void handleInput(BidContext context, String input) {
        if (SUCCESS_BID.name().equals(input)) {
            updateState(context);
            return;
        }
        if (REJECT_BID.name().equals(input)) {
            // Уведомление мастеру
            // Смена статуса bid
            eventPublisher.publishEvent(new MessageSendEvent(this, context.getChatId(), NOTICE_MESSAGE));
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
                        .callbackData(SUCCESS_BID.name())
                        .build(),
                InlineKeyboardButton.builder()
                        .text("Отказаться от услуг исполнителя.")
                        .callbackData(REJECT_BID.name())
                        .build());
    }
}
