package com.workers.business.create_order.process.states;

import com.workers.business.create_order.process.context.OrderContext;

public interface OrderState {
    void handleInput(OrderContext context, String input);
    void enter(OrderContext context);
    void updateState(OrderContext context);
}
