package com.workers.controller.listener.common;

import com.workers.business.common.manager.UserContextManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.workers.controller.events.OrderProcessCompletedEvent;

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
