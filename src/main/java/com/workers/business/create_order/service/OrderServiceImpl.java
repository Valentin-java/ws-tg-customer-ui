package com.workers.business.create_order.service;

import com.workers.business.common.manager.UserContextManager;
import com.workers.business.create_order.interfaces.OrderService;
import com.workers.business.create_order.process.context.OrderContext;
import com.workers.business.create_order.process.states.OrderState;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final UserContextManager contextManager;
    private final OrderState initOrderState;

    public OrderServiceImpl(
            @Qualifier("categorySelectionState") OrderState initOrderState,
            UserContextManager contextManager) {
        this.initOrderState = initOrderState;
        this.contextManager = contextManager;
    }

    @Override
    @SneakyThrows
    public void initProcess(Long chatId, String username) {
        OrderContext context = new OrderContext(chatId);
        context.setCurrentState(initOrderState);
        context.getOrderRequest().setCustomerId(username);
        context.getOrderRequest().setChatId(chatId);
        contextManager.addContext(chatId, context);
        context.getCurrentState().enter(context);
    }
}
