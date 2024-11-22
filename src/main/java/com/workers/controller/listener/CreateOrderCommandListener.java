package com.workers.controller.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.workers.business.create_order.interfaces.OrderService;
import com.workers.controller.events.CommandReceivedEvent;

@Service
@RequiredArgsConstructor
public class CreateOrderCommandListener {

    private static final String CREATE_ORDER_COMMAND = "/create_order";
    private final OrderService service;

    @Async
    @EventListener
    public void handleCreateOrderCommand(CommandReceivedEvent event) {
        if (CREATE_ORDER_COMMAND.equals(event.getCommand())) {
            service.initProcess(event.getChatId(), event.getUsername());
        }
    }
}
