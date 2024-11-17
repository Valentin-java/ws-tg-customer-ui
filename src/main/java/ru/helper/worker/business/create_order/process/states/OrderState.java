package ru.helper.worker.business.create_order.process.states;

import ru.helper.worker.business.create_order.process.context.OrderContext;

public interface OrderState {
    void handleInput(OrderContext context, String input);
    void enter(OrderContext context);
    void updateState(OrderContext context);
}
