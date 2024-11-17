package ru.helper.worker.controller.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.helper.worker.controller.events.OrderProcessCompletedEvent;
import ru.helper.worker.controller.manager.UserContextManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProcessCompletedListener {

    private final UserContextManager contextManager;

    @Async
    @EventListener
    public void handleOrderProcessCompleted(OrderProcessCompletedEvent event) {
        Long chatId = event.getChatId();
        log.info("Order process completed for chatId: {}", chatId);
        contextManager.removeContext(chatId);
    }
}
