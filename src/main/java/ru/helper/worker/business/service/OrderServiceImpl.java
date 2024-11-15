package ru.helper.worker.business.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.helper.worker.business.interfaces.OrderService;
import ru.helper.worker.controller.context.OrderContext;
import ru.helper.worker.controller.events.OrderProcessCompletedEvent;
import ru.helper.worker.controller.state.OrderState;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final Map<Long, OrderContext> userContexts = new ConcurrentHashMap<>();
    private final OrderState initOrderState;

    public OrderServiceImpl(@Qualifier("categorySelectionState") OrderState initOrderState) {
        this.initOrderState = initOrderState;
    }

    @Override
    @SneakyThrows
    public void initProcess(Long chatId, String username) {
        OrderContext context = new OrderContext(chatId);
        context.setCurrentState(initOrderState);
        context.getOrderRequest().setCustomerId(username);
        userContexts.put(chatId, context);
        context.getCurrentState().enter(context);
    }

    @Override
    public OrderContext getContext(Long chatId) {
        return userContexts.get(chatId);
    }

    @EventListener
    public void handleOrderProcessCompleted(OrderProcessCompletedEvent event) {
        Long chatId = event.getChatId();
        log.info("Order process completed for chatId: {}", chatId);
        userContexts.remove(chatId);
    }
}
