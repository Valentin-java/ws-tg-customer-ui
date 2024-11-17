package ru.helper.worker.controller.manager;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.helper.worker.controller.events.MessageSendEvent;
import ru.helper.worker.controller.process.GenericContext;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserContextManager {

    private static final String REJECTED_PROCESS_BY_TIME_EXEED = "Ваш процесс был прерван, так как вы слишком долго его заполняли. " +
            "\n Пожалуйста, начните заново.";

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

            // Проверяем, если контекст устарел
            if (context.getContextCreated().isBefore(LocalDateTime.now().minusHours(1))) {
                eventPublisher.publishEvent(new MessageSendEvent(this, context.getChatId(), REJECTED_PROCESS_BY_TIME_EXEED));
                return true; // Удаляем контекст
            }

            return false; // Контекст остаётся активным
        });
    }
}
