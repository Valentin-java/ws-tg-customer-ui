package ru.helper.worker.business.create_order.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.helper.worker.business.create_order.interfaces.OrderService;
import ru.helper.worker.business.create_order.process.context.OrderContext;
import ru.helper.worker.business.create_order.process.states.OrderState;
import ru.helper.worker.business.common.manager.UserContextManager;
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
