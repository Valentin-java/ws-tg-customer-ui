package ru.helper.worker.business.interfaces;

import ru.helper.worker.controller.context.OrderContext;

public interface OrderService {

    void initProcess(Long chatId, String username);
    OrderContext getContext(Long chatId);
}
