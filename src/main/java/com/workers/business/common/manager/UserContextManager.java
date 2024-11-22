package com.workers.business.common.manager;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.workers.business.create_order.process.context.OrderContext;
import com.workers.business.received_bid.process.context.BidContext;
import com.workers.business.received_bid.process.state.impl.BidReceiveState;
import com.workers.controller.events.MessageEditEvent;
import com.workers.controller.events.MessageSendEvent;
import com.workers.business.common.context.GenericContext;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserContextManager {

    private static final String REJECTED_PROCESS_BY_TIME_EXCEED =
            "Ваш процесс был прерван, так как вы слишком долго его заполняли.\nПожалуйста, начните заново.";
    private static final String REJECTED_BID_PROCESS_BY_TIME_EXCEED =
            "К сожалению, предложение от мастера %s более не актуально.";

    private final Map<Long, GenericContext> userContexts = new ConcurrentHashMap<>();
    private final ApplicationEventPublisher eventPublisher;

    public UserContextManager(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void addContext(Long chatId, GenericContext context) {
        userContexts.put(chatId, context);
    }

    public void removeContext(Long chatId) {
        userContexts.remove(chatId);
    }

    public GenericContext getActiveContext(Long chatId) {
        return userContexts.get(chatId);
    }

    public boolean hasActiveContext(Long chatId) {
        GenericContext context = userContexts.get(chatId);
        return context != null && context.isActive();
    }

    /**
     * Чистим память от процессов пользователей, которые висят по часу.
     */
    @Scheduled(fixedRate = 3600000)
    public void cleanupExpiredContexts() {
        userContexts.entrySet().removeIf(entry -> {
            GenericContext context = entry.getValue();

            if (isExpiredOrderContext(context)) {
                eventPublisher.publishEvent(new MessageSendEvent(this, context.getChatId(), REJECTED_PROCESS_BY_TIME_EXCEED));
                return true;
            }

            if (isExpiredBidContext(context)) {
                handleExpiredBidContext((BidContext) context);
                return true;
            }

            return false; // Контекст остается активным
        });
    }

    /**
     * Проверяет, устарел ли контекст заказа.
     */
    private boolean isExpiredOrderContext(GenericContext context) {
        return context instanceof OrderContext &&
                context.getContextCreated().isBefore(LocalDateTime.now().minusHours(1));
    }

    /**
     * Проверяет, устарел ли контекст предложения.
     */
    private boolean isExpiredBidContext(GenericContext context) {
        return context instanceof BidContext &&
                context.getContextCreated().isBefore(LocalDateTime.now().minusHours(48)) &&
                ((BidContext) context).getCurrentState() instanceof BidReceiveState;
    }

    /**
     * Обрабатывает устаревший контекст предложения.
     */
    private void handleExpiredBidContext(BidContext context) {
        String specialistName = context.getRequest().offer().specialistName();
        String rejectMessage = String.format(REJECTED_BID_PROCESS_BY_TIME_EXCEED, specialistName);

        Integer messageId = context.getMessageIdActualState();
        if (messageId != null) {
            // Публикуем событие для редактирования сообщения
            eventPublisher.publishEvent(new MessageEditEvent(this, context.getChatId(), messageId, rejectMessage));
        }
    }
}
